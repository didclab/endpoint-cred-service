package org.onedatashare.endpointcredentials.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CredListResponse {
    private Set<String> emailList;
}
