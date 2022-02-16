package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;
import static nextstep.subway.acceptance.PathSteps.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RequestParametersSnippet requestParametersSnippet = getRequestParameters();
        ResponseFieldsSnippet responseFieldsSnippet = getResponseFields();


        RestDocumentationFilter filter = 경로관련_문서_필터생성("path", requestParametersSnippet, responseFieldsSnippet);

        //when
        ExtractableResponse<Response> response = 경로조회_및_문서_생성_최단_거리_기준(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }



    @Test
    void pathByDuration() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPathByMinimumTime(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RequestParametersSnippet requestParametersSnippet = getRequestParameters();
        ResponseFieldsSnippet responseFieldsSnippet = getResponseFields();


        RestDocumentationFilter filter = 경로관련_문서_필터생성("pathByDuration", requestParametersSnippet, responseFieldsSnippet);

        //when
        ExtractableResponse<Response> response = 경로조회_및_문서_생성_최소_시간_기준(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }
}