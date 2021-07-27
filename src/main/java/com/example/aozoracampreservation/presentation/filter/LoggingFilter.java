package com.example.aozoracampreservation.presentation.filter;

import com.example.aozoracampreservation.security.user_details.AuthenticatedMember;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * ログ出力パラメータ生成用フィルター
 */
public class LoggingFilter implements Filter {

	// リクエストID
	private static final String KEY_REQUEST_ID = "x-request-id";
	// 会員ID
	private static final String KEY_MEMBER_ID = "memberId";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		putMDC();
		try {
			chain.doFilter(request, response);
		} finally {
			clearMDC();
		}
	}

	private void putMDC() {
		// リクエストID
		String requestId = UUID.randomUUID().toString();
		MDC.put(KEY_REQUEST_ID, requestId);

		// 会員ID
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !"anonymousUser".equals(auth.getName())) {
			// API通信でなく、ログイン済みの場合は設定
			AuthenticatedMember member = (AuthenticatedMember) auth.getPrincipal();
			MDC.put(KEY_MEMBER_ID, String.valueOf(member.getId()));
		}
	}

	private void clearMDC() {
		MDC.remove(KEY_REQUEST_ID);
		MDC.remove(KEY_MEMBER_ID);
	}
}
