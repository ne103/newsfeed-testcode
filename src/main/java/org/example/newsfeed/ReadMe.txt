회원탈퇴 test를 위해  WebSecurityConfig에  .requestMatchers(HttpMethod.PATCH ,"api/members/*").permitAll() 추가! (patch의 저 url은 모두 접근 허가하겠다는!)
state가 Active -> withdraw로 바뀌는거
// 탈퇴한 ID => Status가 withdraw인 걸 찾으면 되잖어~
상태변경시간

   // 중복 ID 체크
        Optional<User> checkUser_id = userRepository.findByUserIdAndStatus(requestDto.getUserId(),
            UserStatusEnum.ACTIVE.name());
        if (checkUser_id.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }