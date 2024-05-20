package ru.alex.testcasebankapp.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record Tokens(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String accessToken,
        @Schema(example = "2024-05-17T18:29:34.123Z")
        String accessTokenExp,
        @Schema(example = "eyJraWQiOiI3N2MwMGFmNy1iMWYyLTQ4OGUtYmM3OC1hOTViYjBlOTY4NTUiLCJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..KUXYg_TsCKmyJnC5.OoL7hvwLOo5G31PIToMqUvjOl-obR0QqQ9XO3SpuYI0xnv4NMv6wCXH3TH3iTYbjTvri3CZ7KVUmsKwz6XBLdCSnvEifebUFHvTf6V4PL9C88mTGNAT7gDw2w5ITGfy-jT8d2j5uNRTbCpiIG0zp6ip0tf-wg1m8f86oxLukH7ZXA_z7DGIEehi-NckRc3al6HPk0K8ol94KuOaWP88.5GovytlV2LdBWFqF0L1urA")
        String refreshToken,
        @Schema(example = "2024-05-17T18:29:34.123Z")
        String refreshTokenExp) {
}
