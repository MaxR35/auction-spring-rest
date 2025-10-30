package fr.rougeux.projet.auction.dto.bo;

import java.time.LocalDateTime;

public class UserDto {

    private long userId;
    private String firstName;
    private String lastName;
    private String userImg;
    private String email;
    private String phone;
    private int credit;
    private LocalDateTime createAt;

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserImg() { return userImg; }
    public void setUserImg(String userImg) { this.userImg = userImg; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }

    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
}

