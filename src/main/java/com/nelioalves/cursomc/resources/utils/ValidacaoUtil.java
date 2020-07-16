package com.nelioalves.cursomc.resources.utils;

import java.util.Collection;
import java.util.HashMap;

public class ValidacaoUtil {
	
	@SuppressWarnings("rawtypes")
    @SafeVarargs
    public static <T, E> boolean isNullOrBlankOrEmpty(T... valor) {

        Boolean resultado = false;
        
        if(valor==null){
            return true;
        }

        if (valor != null && valor instanceof Object[] && ((Object[]) valor).length == 0) {
            resultado = true;
        }

        for (int i = 0; i < valor.length; i++) {

            if (valor[i] != null && valor[i] instanceof Object[] && ((Object[]) valor[i]).length == 0) {
                resultado = Boolean.TRUE;
                break;
            }

            resultado = valor[i] == null || valor[i].equals("") || valor[i].toString() == null || valor[i].toString().trim().equals("");

            if (!resultado) {
                if (valor[i] instanceof Collection<?>) {
                    if (((Collection<?>) valor[i]).size() == 0) {
                        resultado = Boolean.TRUE;
                        break;
                    } else {
                        continue;
                    }
                }

                if (valor[i] instanceof HashMap) {
                    if (((HashMap) valor[i]).size() == 0) {
                        resultado = Boolean.TRUE;
                        break;
                    } else {
                        continue;
                    }
                }

                final Object obj = valor[i];
                if (obj == null) {
                    resultado = Boolean.TRUE;
                    break;
                } else if (obj instanceof Long) {
                    if (Long.valueOf(valor[i].toString()).longValue() == 0L) {
                        resultado = Boolean.TRUE;
                        break;
                    }
                }

            }

            if (resultado) {
                break;
            }

        }
        return resultado;
    }
}
