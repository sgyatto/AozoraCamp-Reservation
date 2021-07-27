package com.example.aozoracampreservation.presentation.signup;

import com.example.aozoracampreservation.application.service.signup.SignupAppService;
import com.example.aozoracampreservation.domain.model.Member;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Locale;

import static com.example.aozoracampreservation.presentation.signup.SignupForm.*;


/**
 * 会員登録 Controller
 */
@Controller
@RequestMapping("/signup")
public class SignupController {

	private final SignupAppService signupAppService;
	private final SignupFormValidator signupFormValidator;
	private final ModelMapper modelMapper;
	private final MessageSource messageSource;

	public SignupController(SignupAppService signupAppService, SignupFormValidator signupFormValidator,
							ModelMapper modelMapper, MessageSource messageSource) {
		this.signupAppService = signupAppService;
		this.signupFormValidator = signupFormValidator;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(signupFormValidator);
	}

	@ModelAttribute(value = "signupForm")
	public SignupForm setUpSignupForm() {
		return new SignupForm();
	}

	/**
	 * 会員登録フォーム表示
	 */
	@GetMapping(params = "form")
	public String signupForm() {
		return "signup/signup";
	}

	/**
	 * 会員登録内容確認
	 */
	@PostMapping(params = "confirm")
	public String signupConfirm(@ModelAttribute("signupForm")
								@Validated({ValidGroup.class, ValidPassword.class}) SignupForm form,
								BindingResult bindingResult, HttpSession session, Model model) {
		if (bindingResult.hasErrors()) {
			return "signup/signup";
		}
		// セッション設定（パスワード）
		session.setAttribute("password", form.getPassword());
		// パスワードのマスク
		String maskedPassword = signupAppService.maskPassword(form.getPassword());
		model.addAttribute("maskedPassword", maskedPassword);
		return "signup/confirm";
	}

	/**
	 * 会員登録
	 */
	@PostMapping()
	public String create(@ModelAttribute("signupForm")
						 @Validated(ValidGroup.class) SignupForm signupForm,
						 HttpServletRequest request,
						 HttpSession session) throws ServletException {

		Member member = modelMapper.map(signupForm, Member.class);
		// セッションからパスワード取得
		String password = (String) session.getAttribute("password");
		member.setPassword(password);
		// 会員登録
		signupAppService.createMember(member);

		// セッションの削除（パスワード）
		session.removeAttribute("password");
		// ログイン
		request.login(signupForm.getMail(), password);
		return "redirect:/";
	}


	@PostMapping(params = "redo")
	public String redo(@ModelAttribute("signupForm") SignupForm signupForm,
					   HttpSession session) {
		// セッション情報の削除
		session.removeAttribute("password");
		return "signup/signup";
	}

	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {
		model.addAttribute("message", messageSource.getMessage("exception.errorAtCreate", null, Locale.JAPAN));
		model.addAttribute("nextUrl", "/signup");
		model.addAttribute("nextViewName", "会員登録画面");
		return "error/uc_error";
	}
}
