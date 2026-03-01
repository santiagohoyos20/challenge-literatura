package com.alura.literatura.service;

public interface IConvertData {
    <T> T obtenerDatos(String json, Class<T> clase);
}
