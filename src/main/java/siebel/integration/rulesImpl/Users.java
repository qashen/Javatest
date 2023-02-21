package siebel.integration.rulesImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private UserDetails[] userDetails;

    public UserDetails[] getUser() {
        return userDetails;
    }

    @JsonProperty("User")
    public void setUser(UserDetails[] userDetails) {
        this.userDetails = userDetails;
    }

    public List<UserDetails> getAllUserByAccountNumber(Long accountNumber){
        return Arrays.stream(getUser()).filter(c -> c.accountNumber.toString().equals(accountNumber)).collect((Collectors.toList()));
    }

    public List<UserDetails> getAllUser(){
        return Arrays.stream(getUser()).collect((Collectors.toList()));
    }
}
