package Youtrack;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Cookies;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class IssueMethods {
Cookies cookies;
    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "https://goit2.myjetbrains.com/youtrack/rest";

        Response response =
                given().
                        param("login", "klimenko.oles@mail.ru").
                        param("password", "oles12").
                        when().
                        post("/user/login");

        cookies = response.getDetailedCookies();
    }

    @Test
    public void testCreateIssue() throws Exception {
given().
        cookies(cookies).
        param("project", "GOITQA").
        param("summary", "Some summary").
        param("description", "My description").
            when().
                put("/issue").
            then().
                statusCode(201);

    }


    private String createTestIssue() throws Exception{
        Response response =
                given().
                        cookies(cookies).
                        param("project", "GOITQA").
                        param("summary", "Some summary").
                        param("description", "Some description").
                        when().
                        put("/issue");
                    String location = response.getHeader("location");
        String issueId = location.substring(location.lastIndexOf('/') + 1);
        return issueId;
}


    @Test
    public void testDeleteIssue() throws Exception {
      String issueID = createTestIssue();

        given().
                cookies(cookies).
                when().
                    delete("/issue/" + issueID).
                then().
                    statusCode(200);

    }

    @Test
    public void testGetIssue() throws Exception {
        String issueID = createTestIssue();

Response response =         //для сохранения ответа
        given().
            cookies(cookies).
        when().
            get("/issue/" + issueID).
        then().
            statusCode(200).
                body("issue.@id", equalTo(issueID)).
        extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void testIssueExist() throws Exception {
        String issueID = createTestIssue();
        given().
                cookies(cookies).
                when().
                get("/issue/"+ issueID +"/exists").
                then().statusCode(200);
    }

    @Test
    public void testIssueNotExists() throws Exception {
        String issueID = "12345"; //негативныей кейс, передаем неверное значение в ID
        given().
                cookies(cookies).
                when().
                get("/issue/" + issueID + "/exists").
                then().statusCode(404);

    }

    @Test
    public void testGetNumberOfIssues() throws Exception {
        Response response =
        given().
                cookies(cookies).
                param("filter", "10").
                when().
                get("/issue/count").
                then().
                statusCode(200).
                exctract().response()
               System.out.print(response.asString());

        Integer issueNumber = Integer.parseInt(response.asString().replaceALl("[\\D]", ""));
        System.out.println(issueNumber);
        assertThat(issueNumber, greaterThanOrEqualTo(10));
    }

    private String countIssues() {
  return null;
   }


    @Test
    public void testUpdateIssue() throws Exception {
        given().
                cookies(cookies).
                param("project", "GOITQA").
                param("summary", "Updated summary").
                param("description", "Updated description").
                when().
                post("/issue" + issueID).
                then().
                statusCode(200);

    }



}

//ДЗ - доделать  остальные методы по данному примеру!!!
