package com.rohit.pg.simulator.dto;

import com.rohit.pg.simulator.enums.TokenType;

public record TokenInfo(TokenType type, String issuedFor) {}
