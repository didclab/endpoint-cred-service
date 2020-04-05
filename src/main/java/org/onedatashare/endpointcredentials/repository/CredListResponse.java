package org.onedatashare.endpointcredentials.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CredListResponse {
    private List<String> list;
}
