package com.SocialNetwork.security;

import java.security.Principal;

public class StompPrincipal implements Principal {
	private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		 return this.name;
	}

}
