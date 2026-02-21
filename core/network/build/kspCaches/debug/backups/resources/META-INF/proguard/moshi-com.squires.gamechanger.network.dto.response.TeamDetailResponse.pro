-keepnames class com.squires.gamechanger.network.dto.response.TeamDetailResponse
-if class com.squires.gamechanger.network.dto.response.TeamDetailResponse
-keep class com.squires.gamechanger.network.dto.response.TeamDetailResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
