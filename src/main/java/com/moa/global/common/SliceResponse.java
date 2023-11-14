package com.moa.global.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;


@Getter
@AllArgsConstructor
public class SliceResponse<T> {

	List<T> content;
	boolean first;
	boolean last;
	int number;
	int size;
	int numberOfElements;


	// Slice 객체에서 필요한 데이터만 추출하여 SliceResponse 객체를 생성한다.
	public static <T> SliceResponse<T> of(Slice<T> slice) {
		return new SliceResponse<>(slice.getContent(), slice.isFirst(), slice.isLast(), slice.getNumber(), slice.getSize(), slice.getNumberOfElements());
	}

}
