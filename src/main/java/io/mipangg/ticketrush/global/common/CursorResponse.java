package io.mipangg.ticketrush.global.common;

import java.util.List;

public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,   // 다음 요청에 넘길 커서 (없으면 null)
        boolean hasNext
) {}
