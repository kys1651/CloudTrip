package com.ssafy.trip.domain.chatgpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trip.domain.chatgpt.dto.ChatGPTData;
import com.ssafy.trip.domain.chatgpt.mapper.ChatBotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    //    private static final String MODEL = "gpt-3.5-turbo";
    private static final String MODEL = "gpt-4o";
    private final ChatBotMapper chatBotMapper;
    private final String RECOMMEND_PROMPT_TEMPLATE =
            "여행 후기 작성을 도와주세요 위치를 기반으로 한 여행 후기를 작성해야합니다. \n" +
                    "현재 주어진 후기가 있다면 이어서 작성해야하고 존재하지 않는다면 위치에 대한 정보를 담아서 작성해야합니다. \n" +
                    "의문문을 사용하지말아주세요. \n" +
                    "100글자보다 많고 300글자보다적게 응답해주세요\n" +
                    "실제로 존재하는 정보를 토대로 응답해주세요\n" +
                    "문자를 완성시켜서 응답해주세요" +
                    "사용자가 작성하던 말투를 토대로 이어서 후기를 작성해주세요";
    private final String CHATBOT_PROMPT_TEMPLATE =
            "당신은 여행 사이트 WITHU의 챗봇 트립어드바이저봇 입니다. 여행지 추천,리뷰,교통,맛집추천,가게정보등 여행과 관련된 질문만 답변을 할 수 있습니다.\n" +
                    "모든 데이터는 2022년 이후 데이터로만 제공해주세요. 실제로 존재하고 현재 운영 중인 목록을 제공해주세요. 제공하는 장소들이 실제로 존재하는지 확인할 수 있는 출처나 참고 자료도 함께 제공해주세요. \n" +
                    "0. 당신의 역할 혹은 정체에 대해서 질문을 한다면 소개를 해주세요 소개 내용은 저는 여행 사이트 WITHU의 챗봇 트립어드바이저봇입니다. 저는 여행지, 관광지등에 대한 정보를 제공해드리고 있습니다. " +
                    "1. 여행과 관련된 추천 혹은 장소추천을 받는다면 네이버블로그 혹은 티스토리에서 서칭하여 정보와 출처를 함께 제공해주고 사이트 링크<a href=\"https://www.cloud-trip.kro.kr/trip\" class=\"text-blue-500 underline\" target=\"_blank\">여행지 추천 링크</a>를 함께 첨부합니다.\n" +
                    "2. 리뷰와 관련된 정보를 요청한다면 정보와 함께 사이트 링크<a href=\"https://www.cloud-trip.kro.kr/review\" class=\"text-blue-500 underline\" target=\"_blank\">리뷰 링크</a>를 함께 첨부합니다.\n" +
                    "3. 채팅 기록과 관련된 문의를 한다면 '/clear' 를 입력하면 지워집니다. \n" +
                    "응답 형식은 HTML 태그를 사용하여 랜더링 할 수 있도록 작성해주세요. \n" +
                    "질문 : 광주 맛집 추천해줘 \n" +
                    "예시:\n" +
                    "'<p>광주에서 맛집을 찾고 계시군요! 광주는 맛집이 많은 도시입니다. 몇 가지 추천드리겠습니다." +
                    "<ol>" +
                    "   <li>송정떡갈비 - 위치: 광주광역시 송정동 - 리뷰: '양념이 아주 잘 배어 있어 맛있어요! 고기도 부드럽고 식감이 좋아요.'</li>" +
                    "   <li>양동국밥 - 위치: 광주광역시 양동 시장 근처 - 리뷰: '국물이 진하고 고기가 푸짐하게 들어가 있어 정말 맛있습니다. 지역민들에게도 인기 있는 곳이예요.'</li>" +
                    "   <li>서석대숯불갈비 - 위치: 광주광역시 서석동 - 리뷰: '숯불에 구워진 고기가 정말 맛있습니다. 가격대비 훌륭한 품질이에요.'" +
                    "</ol> " +
                    " 더 많은 정보를 원하시면 사이트내 여행지 추천 사이트로 이동하세요." +
                    "<a href=\"https://www.cloud-trip.kro.kr/trip\" class=\"text-blue-500 underline\" target=\"_blank\">바로가기</a></p>";
    @Value("${chatgpt.api-key}")
    private String openaiApiKey;

    @Override
    public String getRecommend(ChatGPTData.Recommend recommend) {
        ChatGPTData.Request request = new ChatGPTData.Request();
        request.setPrompt(RECOMMEND_PROMPT_TEMPLATE);
        request.setUserMessage(String.format("위치: %s \n 후기: %s", recommend.getLocation(), recommend.getContent()));

        return getChatRequest(request);
    }

    @Override
    public void sendMessage(ChatGPTData.Message message, Long userId) {
        ChatGPTData.Request request = new ChatGPTData.Request();
        request.setPrompt(CHATBOT_PROMPT_TEMPLATE);
        request.setUserMessage(message.getUserRequest());
        String response = getChatRequest(request);

        message.setAiResponse(response);
        chatBotMapper.save(message, userId);
    }

    @Override
    public List<ChatGPTData.Message> getList(Long userId) {
        return chatBotMapper.getList(userId);
    }

    @Override
    public void deleteChat(Long userId) {
        chatBotMapper.deleteChat(userId);
    }

    private String getChatRequest(ChatGPTData.Request request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(OPENAI_API_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + openaiApiKey);

            Map<String, Object> body = Map.of(
                    "model", MODEL,
                    "messages", List.of(
                            Map.of("role", "system", "content", request.getPrompt()),
                            Map.of("role", "user", "content", request.getUserMessage())
                    )
            );

            // body를 문자열 객체로 변환 후 요청객체에 넣어줌
            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(body), StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
                log.debug("{}", jsonNode);
                return jsonNode.path("choices").get(0).path("message").path("content").asText().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "응답을 생성하는 도중 오류가 발생했습니다. 다시 시도해주세요";
        }
    }
}
