-keepnames class com.squires.gamechanger.network.dto.response.TeamResponse
-if class com.squires.gamechanger.network.dto.response.TeamResponse
-keep class com.squires.gamechanger.network.dto.response.TeamResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
