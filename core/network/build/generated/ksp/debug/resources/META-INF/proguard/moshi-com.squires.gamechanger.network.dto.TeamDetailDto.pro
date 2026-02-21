-keepnames class com.squires.gamechanger.network.dto.TeamDetailDto
-if class com.squires.gamechanger.network.dto.TeamDetailDto
-keep class com.squires.gamechanger.network.dto.TeamDetailDtoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
