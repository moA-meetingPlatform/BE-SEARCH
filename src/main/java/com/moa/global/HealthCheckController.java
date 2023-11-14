package com.moa.global;


import com.moa.global.common.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/search")
@Slf4j
@RequiredArgsConstructor
public class HealthCheckController {

	@Operation(hidden = true)
	@GetMapping("/test")
	public ResponseEntity<ApiResult<String>> test() {
		return ResponseEntity.ok(ApiResult.ofSuccess("health check"));
	}

}
