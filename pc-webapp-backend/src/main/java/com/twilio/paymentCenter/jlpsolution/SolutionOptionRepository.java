package com.twilio.paymentCenter.jlpsolution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionOptionRepository extends JpaRepository<SolutionOption, Long> {

    Optional<SolutionOption> findByPreviousAndPhoneId(final String previous, final String phoneId);

    Optional<SolutionOption> findByPrevious(final String previous);
}
