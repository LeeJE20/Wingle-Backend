package kr.co.wingle.member;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.member.dto.AcceptanceRequestDto;
import kr.co.wingle.member.dto.AcceptanceResponseDto;
import kr.co.wingle.member.dto.WaitingListResponseDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;

	@GetMapping("/list/waiting/{page}")
	public ApiResponse<List<WaitingListResponseDto>> waitingList(@PathVariable int page) {
		checkAdminAccount();
		List<WaitingListResponseDto> response = memberService.getWaitingList(page);
		return ApiResponse.success(SuccessCode.WAITING_LIST_READ_SUCCESS, response);
	}

	@PostMapping("/permission/acceptance")
	public ApiResponse<AcceptanceResponseDto> accept(@RequestBody @Valid AcceptanceRequestDto acceptanceRequestDto) {
		checkAdminAccount();
		AcceptanceResponseDto response = authService.accept(acceptanceRequestDto);
		return ApiResponse.success(SuccessCode.ACCEPTANCE_SUCCESS, response);
	}

	private void checkAdminAccount() {
		Member member = authService.findMember();
		if (member.getAuthority() == Authority.ROLE_USER) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
		}
	}
}
