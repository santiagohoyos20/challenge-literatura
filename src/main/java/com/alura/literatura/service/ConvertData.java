package com.alura.literatura.service;


import tools.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json,clase);
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }
}