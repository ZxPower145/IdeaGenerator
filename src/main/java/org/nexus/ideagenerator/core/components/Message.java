package org.nexus.ideagenerator.core.components;

import org.nexus.ideagenerator.core.components.enums.Role;

public class Message {
    private Role role;
    private String content;

    public Message(Role role, String content) {
        this.role = role;
        this.content = content;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                    "role" + ": " + role.toString() + ", " +
                    "content" + ": " + content +
                "}";
    }
}
