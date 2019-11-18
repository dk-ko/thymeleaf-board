package com.example.demo.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CommonAuditorAware implements AuditorAware<String>
{
    // TODO spring security 적용 후 유저 정보를 가져와서 등록하는 식으로 변경할 것.
    // https://blusky10.tistory.com/316
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
    }
}
