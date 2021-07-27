package com.example.aozoracampreservation.aspect;

import com.example.aozoracampreservation.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ログ出力用Aspectクラス
 */
@Aspect
@Slf4j
@Component
public class LoggingAspects {

	/**
	 * Controller用Pointcut
	 */
	@Pointcut("@within(org.springframework.stereotype.Controller)" +
			" || @within(org.springframework.web.bind.annotation.RestController)")
	private void controllerPointcut() {}

	/**
	 * Service用Pointcut
	 */
	@Pointcut("@within(org.springframework.stereotype.Service)")
	private void servicePointcut() {}

	/**
	 * Publicメソッド用Pointcut
	 */
	@Pointcut("execution(public * com.example.aozoracampreservation..*.*(..))")
	private void publicMethodPointcut() {}

	/**
	 * メソッド開始ログ（Controller）
	 * @param jp JoinPoint
	 */
	@Before("controllerPointcut() && publicMethodPointcut()")
	public void controllerStartLog(JoinPoint jp) {
		log.info("method start. signature={}",jp.getSignature());
	}

	/**
	 * メソッド開始ログ（Service）
	 * @param jp JoinPoint
	 */
	@Before("servicePointcut() && publicMethodPointcut()")
	public void serviceStartLog(JoinPoint jp) {
		log.info("method start. signature={} args={}",jp.getSignature(), Arrays.toString(jp.getArgs()));
	}

	/**
	 * メソッド終了ログ（Controller, Service）
	 * @param jp JoinPoint
	 */
	@AfterReturning("controllerPointcut() || servicePointcut() && publicMethodPointcut()")
	public void endLog(JoinPoint jp) {
		log.info("method end.   signature={}",jp.getSignature());
	}

	/**
	 * 例外発生時ログ
	 * @param jp JoinPoint
	 * @param t Throwable
	 */
	@AfterThrowing(value = "controllerPointcut()", throwing = "t")
	public void throwableLog(JoinPoint jp, Throwable t) {
		if (t instanceof BusinessException) {
			log.warn("business exception stack trace.", t);
		} else {
			log.error("throwable stack trace.", t);
		}
	}

	/**
	 * 予約完了ログ
	 * @param jp JoinPoint
	 */
	@AfterReturning("execution(* com.example.aozoracampreservation.domain.service." +
			"ReservationDetailService.createReservationDetails(..))")
	public void reservationCompleteLog(JoinPoint jp) {
		Object[] args = jp.getArgs();
		log.info("Reservation complete. reservationId={}", args[1].toString());
	}

}
