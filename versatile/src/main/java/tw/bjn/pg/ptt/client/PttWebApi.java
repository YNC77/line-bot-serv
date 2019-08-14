package tw.bjn.pg.ptt.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PttWebApi {

    @GET("bbs/index.html")
    Call<ResponseBody> getHotBoards();

    @GET("bbs/{board}/index{index}.html")
    Call<ResponseBody> getPosts(@Path("board") String board, @Path("index") String index);

    @GET("bbs/{board}/search")
    Call<ResponseBody> searchBoard(@Path("board") String board, @Query("q") String q, @Query("page") String page);

    @GET("{path}")
    Call<ResponseBody> fetchUrl(@Path("path") String path);
}
