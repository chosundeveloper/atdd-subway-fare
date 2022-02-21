package nextstep.subway.documentation;

import static nextstep.subway.acceptance.AuthAcceptanceTest.AGE_ADULT;
import static nextstep.subway.acceptance.AuthAcceptanceTest.EMAIL;
import static nextstep.subway.acceptance.AuthAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회하기_문서화_스펙_정의;
import static nextstep.subway.acceptance.PathSteps.경로조회의_결과_경로가_예상과_같다;
import static nextstep.subway.acceptance.PathSteps.경로조회의_결과_정보가_예상과_같다;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    private final int distance = 9;
    private final int duration = 10;
    private final int lineFare = 0;
    private final String startTime = "0530";
    private final String endTime = "2330";
    private final int intervalTime = 10;
    private final String time = "202202200600";

    @Test
    void pathTypeDistance() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_ADULT);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        spec = 경로_조회하기_문서화_스펙_정의(restDocumentation);

        StationResponse 교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);
        지하철_노선_생성_요청("이호선", "이호선색상", 교대역.getId(), 양재역.getId()
            , distance, duration, lineFare, startTime, endTime, intervalTime);

        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역.getId())
            .queryParam("target", 양재역.getId())
            .queryParam("pathType", PathType.DISTANCE)
            .queryParam("time", time)
            .when().get("/paths")
            .then().assertThat().statusCode(is(200))
            .log().all().extract();

        경로조회의_결과_경로가_예상과_같다(response, 교대역.getId(), 양재역.getId());
        경로조회의_결과_정보가_예상과_같다(response, distance, duration, 1250);
    }
}
