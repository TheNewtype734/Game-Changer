-keepnames class com.squires.gamechanger.network.dto.response.LeagueResponse
-if class com.squires.gamechanger.network.dto.response.LeagueResponse
-keep class com.squires.gamechanger.network.dto.response.LeagueResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
