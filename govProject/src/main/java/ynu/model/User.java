/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ynu.model;


import javax.persistence.*;
/**
 *
 * @author LaiWenGen
 */
@Entity
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @Column
        private String email;

        @Column
        private String password;

        @Column
        private String role;

        @Column 
        private String state;

        @Column 
        private String realName;

        @Column 
        private String lastLoginTime;

        public Long getId(){
                return id;
        }

        public void setId(){
                id = this.id;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword(){
                return password;
        }

        public void setPassword(String password){
                this.password = password;
        }

        public String getRole() {
                return role;
        } 

        public void setRole(String role){
                this.role = role;
        }

        public String getState() {
                return state;
        }

        public void setState(String state) {
                this.state = state;
        }

        public String getRealName() {
                return realName;
        }

        public void setRealName(String realName) {
                this.realName = realName;
        }

        public String getLastLoginTime() {
                return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
        }
    
    
    
}
