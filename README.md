1.회원가입

frontend:	
	component: https://github.com/injun0607/URUNNER/blob/main/frontend/src/components/MemberRegisterForm.vue

backend: 
	controller: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/controller/member/MemberController.java
	service: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/service/member/MemberServiceImpl.java


2.이메일인증

backend:
	controller: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/controller/myPage/MyPageController.java
	MailUtil: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/utility/MailUtils.java


3.회원탈퇴


backend:
	controller: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/controller/member/MemberController.java
	service: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/service/member/MemberServiceImpl.java


4.최근학습한강의

frontend:
	view: https://github.com/injun0607/URUNNER/blob/main/frontend/src/views/mypage/MyLatestPage.vue
	component: https://github.com/injun0607/URUNNER/blob/main/frontend/src/components/mypage/MyLatestLecture.vue

backend:
	controller: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/controller/myPage/MyLectureController.java
	service: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/service/board/CallLectureServiceImpl.java


5.결제시스템

frontend:
	view: https://github.com/injun0607/URUNNER/blob/main/frontend/src/views/payment/PaymentSuccess.vue
	component: https://github.com/injun0607/URUNNER/blob/main/frontend/src/components/payment/PaymentBox.vue

backend: 
	controller: https://github.com/injun0607/URUNNER/blob/main/backend/khweb/src/main/java/com/urunner/khweb/controller/payment/PaymentController.java
	service: https://github.com/injun0607/URUNNER/tree/main/backend/khweb/src/main/java/com/urunner/khweb/service/payment
