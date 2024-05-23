package com.ssafy.trip.core.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
@Service
public class S3UploadService {
    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Async("imageUploadExecutor")
    public CompletableFuture<String> upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }
        return CompletableFuture.completedFuture(this.uploadImage(image));
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            s3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_FILE);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_EXTENTION);
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_EXTENTION);
        }
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            return this.uploadToS3(image);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_TO_CREATE_FILE);
        }
    }

    private String uploadToS3(MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String extention = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFileName;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_CREATE_FILE);
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return s3.getUrl(bucketName, s3FileName).toString();
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1);
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.FAIL_TO_DELETE_FILE);
        }
    }
}
