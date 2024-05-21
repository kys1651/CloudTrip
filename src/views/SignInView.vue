<script setup lang="ts">
import FindPasswordConfirmForm from "@/components/auth/FindPasswordConfirmForm.vue";
import FindPasswordForm from "@/components/auth/FindPasswordForm.vue";
import FindPasswordResetForm from "@/components/auth/FindPasswordResetForm.vue";
import SignInForm from "@/components/auth/SignInForm.vue";
import SignUpForm from "@/components/auth/SignUpForm.vue";
import HomeNavigator from "@/components/common/HomeNavigation.vue";
import { FindPasswordStatus, FormStatus } from "@/types/auth.type";
import { MoveLeft } from "lucide-vue-next";
import { ref } from "vue";

const status = ref<FormStatus>(FormStatus.SIGN_IN);

const changeStatus = (newStatus: FormStatus) => {
  status.value = newStatus;
};

const passwordStatus = ref<FindPasswordStatus>(FindPasswordStatus.EMAIL);

const changePasswordStatus = (newStatus: FindPasswordStatus) => {
  passwordStatus.value = newStatus;
};
const findPasswordEmail = ref<string>("");
const changeFindPasswordEmail = (email: string) => {
  findPasswordEmail.value = email;
};
</script>
<template>
  <div class="absolute h-screen flex items-center">
    <div class="fixed top-5 left-10"><HomeNavigator /></div>
    <!-- 로그인 화면 -->
    <transition name="fade" mode="out-in">
      <div
        v-if="status === FormStatus.SIGN_IN"
        class="max-h-[1200px] h-full w-screen flex flex-row items-center justify-center md:grid lg:max-w-none lg:grid-cols-2 lg:px-0"
        key="sign-in"
      >
        <div class="lg:p-8">
          <div class="mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]">
            <div class="flex flex-col space-y-2 text-center">
              <h1 class="text-2xl font-semibold tracking-tight">로그인</h1>
              <p class="text-sm text-muted-foreground">
                저희 서비스를 이용하려면 로그인이 필요합니다.
              </p>
            </div>
            <SignInForm @change-status="changeStatus" />
          </div>
        </div>
        <div
          class="relative hidden h-full flex-col bg-muted p-10 text-white dark:border-r lg:flex"
        >
          <div
            class="absolute inset-0 bg-cover bg-center bg-no-repeat"
            style="background-image: url('/sign-in-bg.jpeg')"
          />
          <div class="relative z-20 mt-auto">
            <blockquote class="space-y-2">
              <p class="text-lg text-gray-500 italic font-bold">
                &ldquo;여행을 통해 홀로 있음을 알게 되고, 홀로 있음을 통해 세상과 연결되는
                법을 배운다. 여행은 자신이 살아 있음을 가장 잘 증명해주는 것. 여행 중일 때
                나는 그 어느 때보다 나 자신일 수가 있다.&rdquo;<br />
                <small>- 지구별 여행자, 류시화</small>
              </p>
            </blockquote>
          </div>
        </div>
      </div>
    </transition>

    <!-- 회원가입 화면 -->
    <transition name="fade" mode="in-out">
      <div
        v-if="status === FormStatus.SIGN_UP"
        class="max-h-[1200px] h-full w-screen flex flex-row items-center justify-center md:grid lg:max-w-none lg:grid-cols-2 lg:px-0"
        key="sign-up"
      >
        <div
          class="w-full relative hidden h-full flex-col bg-muted p-10 text-white dark:border-r lg:flex"
        >
          <div
            class="absolute inset-0 bg-cover bg-center bg-no-repeat w-full"
            style="background-image: url('/sign-up-bg.jpeg')"
          />
          <div class="relative z-20 mt-auto">
            <blockquote class="space-y-2">
              <p class="text-lg text-white italic font-bold">
                &ldquo;소중한 것을 깨닫는 장소는 언제나 컴퓨터 앞이 아니라 파란 하늘
                아래였다.&rdquo;<br />
                <small>-소설가, 다카하시 아유무</small>
              </p>
            </blockquote>
          </div>
        </div>
        <div class="lg:p-8">
          <div class="mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]">
            <div
              class="flex flex-row text-gray-400 items-center cursor-pointer hover:text-gray-500"
              @click="changeStatus(FormStatus.SIGN_IN)"
            >
              <MoveLeft :size="15" /> <span class="ml-2">로그인 하러가기</span>
            </div>
            <div class="flex flex-col space-y-2 text-center">
              <h1 class="text-2xl font-semibold tracking-tight">회원가입</h1>
              <p class="text-sm text-muted-foreground">
                저희 서비스를 이용하려면 회원가입이 필요합니다.
              </p>
            </div>
            <SignUpForm @change-status="changeStatus" />
          </div>
        </div>
      </div>
    </transition>

    <!-- 비밀번호 찾기 화면 -->
    <transition name="fade" mode="in-out">
      <div
        v-if="status === FormStatus.FIND_PASSWORD"
        class="max-h-[1200px] h-full w-screen flex flex-row items-center justify-center md:grid lg:max-w-none lg:grid-cols-2 lg:px-0"
        key="find-password"
      >
        <div
          class="w-full relative hidden h-full flex-col bg-muted p-10 text-white dark:border-r lg:flex"
        >
          <div
            class="absolute inset-0 bg-cover bg-center bg-no-repeat w-full"
            style="background-image: url('/find-password-bg.jpg')"
          />
          <div class="relative z-20 mt-auto">
            <blockquote class="space-y-2">
              <p class="text-lg text-gray-200 italic font-bold">
                &ldquo;목적지에 닿아야 행복해지는 것이 아니라 여행하는 과정에서 행복을
                느낀다.&rdquo;<br />
                <small>-작가, 앤드류 매튜스</small>
              </p>
            </blockquote>
          </div>
        </div>
        <div class="lg:p-8">
          <div class="mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]">
            <div
              class="flex flex-row text-gray-400 items-center cursor-pointer hover:text-gray-500"
              @click="changeStatus(FormStatus.SIGN_IN)"
            >
              <MoveLeft :size="15" /> <span class="ml-2">로그인 하러가기</span>
            </div>
            <FindPasswordForm
              v-if="passwordStatus === FindPasswordStatus.EMAIL"
              @password-change="changePasswordStatus"
              @search-email="changeFindPasswordEmail"
            />
            <FindPasswordConfirmForm
              v-else-if="passwordStatus === FindPasswordStatus.CONFIRM"
              :email="findPasswordEmail"
              @password-change="changePasswordStatus"
            />
            <FindPasswordResetForm
              :email="findPasswordEmail"
              v-else
              @change-status="changeStatus"
            />
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  opacity: 1;
  transition: opacity 1s ease-in-out;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
  transition: opacity 1s ease-in-out;
}
</style>
