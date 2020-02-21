package com.zj.imcore.apis.group;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 讨论组接口
 *
 * @author yangji
 */
public interface GroupApiService {

    /**
     * 创建 讨论组
     *
     * @param body 数据
     * @return 讨论组详情
     */
    @POST("/relation/v1/dialogs")
    Observable<String> createDialog(@Body CreateDialog body);

    /**
     * 获取 讨论组详情
     *
     * @param dialogId dialogId
     * @return 讨论组详情
     */
    @GET("/relation/v1/dialogs/{dialogId}}")
    Observable<String> queryDialog(@Path("dialogId") String dialogId);

    /**
     * 修改 讨论组 信息
     *
     * @param dialogId 讨论组 Id
     * @param body     讨论组内容
     * @return 讨论组编辑成功，完整详情
     */
    @PATCH("/relation/v1/users/{dialogId}")
    Observable<String> update(@Path("dialogId") String dialogId, @Body Object body);


    /**
     * 根据 讨论组 Id 获取 讨论组成员
     *
     * @param dialogId 讨论组 Id
     * @return 讨论组成员
     * [
     * {
     * "tmid": "=bw52P",
     * "role": "normal"
     * },.....
     */
    @GET("/relation/v1/dialogs/{dialogId}/members")
    Observable<String> queryDialogMembers(@Path("dialogId") String dialogId);


    /**
     * 邀请成员假如讨论组（接口不对）
     *
     * @param dialogId 讨论组编号
     * @return 没有内容
     */
    @POST("/relation/v1/dialogs/{dialogId}}/members")
    Observable<String> addUserToDialog(@Path("dialogId") String dialogId);


    /**
     * 提出 or 主动退出讨论组(接口已经测试通过)
     *
     * @param dialogId 对话 id
     * @param teamId   团队 id
     * @param action   kick or leave
     * @return 没有内容
     */
    @DELETE("/relation/v1/dialogs/{dialogId}/members/{teamId}?action={action}")
    Observable<String> removeUserToDialog(@Path("dialogId") String dialogId,
                                          @Path("teamId") String teamId,
                                          @Path("action") String action);
}
