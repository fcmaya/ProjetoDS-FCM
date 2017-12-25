package br.com.documentsolutions.projetods.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapUtil {

    public static Bitmap carregarBitmap(File imgFile, int alturaDesejada, int larguraDesejada){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

        final int altura = options.outHeight;
        final int largura = options.outWidth;

        if (altura > alturaDesejada || largura > larguraDesejada){

            final int taxaAltura = Math.round((float) altura / (float)alturaDesejada);
            final int taxaLargura = Math.round((float) largura / (float)larguraDesejada);

            options.inSampleSize = taxaAltura < taxaLargura ? taxaAltura : taxaLargura;
        }

        options.inJustDecodeBounds = false;

        return  BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
    }

}
