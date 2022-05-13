package com.gertec.exemplosgertec.ExemploImpressora;

public class Language {
    static byte [] Portugues(int CodePageChoice, String linha) {
        //1= Page3
        //2=Page80

        byte [] Retorno = new byte[linha.length()];
        int i = 0;
        for (Character c : linha.toCharArray()) {
            if(CodePageChoice == 1) {
                switch (c) {
                    case 'Á':
                        c = 134;
                        break;
                    case 'á':
                        c = 160;
                        break;
                    case 'à':
                        c = 133;
                        break;
                    case 'À':
                        c = 145;
                        break;
                    case 'â':
                        c = 131;
                        break;
                    case 'Â':
                        c = 143;
                        break;
                    case 'ã':
                        c = 132;
                        break;
                    case 'Ã':
                        c = 142;
                        break;
                    case 'Ç':
                        c = 128;
                        break;
                    case 'ç':
                        c = 135;
                        break;
                    case 'é':
                        c = 130;
                        break;
                    case 'É':
                        c = 144;
                        break;
                    case 'è':
                        c = 138;
                        break;
                    case 'È':
                        c = 146;
                        break;
                    case 'ê':
                        c = 136;
                        break;
                    case 'Ê':
                        c = 137;
                        break;
                    case 'Í':
                        c = 139;
                        break;
                    case 'í':
                        c = 161;
                        break;
                    case 'ì':
                        c = 141;
                        break;
                    case 'Ì':
                        c = 152;
                        break;
                    case 'Ó':
                        c = 159;
                        break;
                    case 'ó':
                        c = 162;
                        break;
                    case 'ò':
                        c = 149;
                        break;
                    case 'Ô':
                        c = 140;
                        break;
                    case 'ô':
                        c = 147;
                        break;
                    case 'õ':
                        c = 148;
                        break;
                    case 'Õ':
                        c = 153;
                        break;
                    case 'Ú':
                        c = 150;
                        break;
                    case 'ú':
                        c = 163;
                        break;
                    case 'ù':
                        c = 151;
                        break;
                    case 'Ù':
                        c = 157;
                        break;
                }
            }else{
                switch (c) {
                    case 'À':c = 0xC0;break;
                    case 'Á':c = 0xC1;break;
                    case 'Â':c = 0xC2;break;
                    case 'Ã':c = 0xC3;break;
                    case 'Ä':c = 0xC4;break;
                    case 'Å':c = 0xC5;break;
                    case 'Æ':c = 0xC6;break;
                    case 'Ç':c = 0xC7;break;
                    case 'È':c = 0xC8;break;
                    case 'É':c = 0xC9;break;
                    case 'Ê':c = 0xCA;break;
                    case 'Ë':c = 0xCB;break;
                    case 'Ì':c = 0xCC;break;
                    case 'Í':c = 0xCD;break;
                    case 'Î':c = 0xCE;break;
                    case 'Ï':c = 0xCF;break;
                    case 'Ð':c = 0xD0;break;
                    case 'Ñ':c = 0xD1;break;
                    case 'Ò':c = 0xD2;break;
                    case 'Ó':c = 0xD3;break;
                    case 'Ô':c = 0xD4;break;
                    case 'Õ':c = 0xD5;break;
                    case 'Ö':c = 0xD6;break;
                    case 'Œ':c = 0xD7;break;
                    case 'Ø':c = 0xD8;break;
                    case 'Ù':c = 0xD9;break;
                    case 'Ú':c = 0xDA;break;
                    case 'Û':c = 0xDB;break;
                    case 'Ü':c = 0xDC;break;
                    case 'Ý':c = 0xDD;break;
                    case 'Þ':c = 0xDE;break;
                    case 'ß':c = 0xDF;break;
                    case 'à':c = 0xE0;break;
                    case 'á':c = 0xE1;break;
                    case 'â':c = 0xE2;break;
                    case 'ã':c = 0xE3;break;

                    case 'ä':c = 0xE4;break;
                    case 'å':c = 0xE5;break;
                    case 'æ':c = 0xE6;break;
                    case 'ç':c = 0xE7;break;

                    case 'è':c = 0xE8;break;
                    case 'é':c = 0xE9;break;
                    case 'ê':c = 0xEA;break;
                    case 'ë':c = 0xEB;break;
                    case 'ì':c = 0xEC;break;
                    case 'í':c = 0xED;break;
                    case 'î':c = 0xEE;break;
                    case 'ï':c = 0xEF;break;

                    case 'ð':c = 0xF0;break;
                    case 'ñ':c = 0xF1;break;
                    case 'ò':c = 0xF2;break;
                    case 'ó':c = 0xF3;break;
                    case 'ô':c = 0xF4;break;
                    case 'õ':c = 0xF5;break;
                    case 'ö':c = 0xF6;break;
                    case 'ø':c = 0xF8;break;
                    case 'ù':c = 0xF9;break;
                    case 'ú':c = 0xFA;break;
                    case 'û':c = 0xFB;break;
                    case 'ü':c = 0xFC;break;
                    case 'ý':c = 0xFD;break;
                    case 'þ':c = 0xFE;break;
                    case 'ÿ':c = 0xFF;break;
                }

            }
            int j = c;
            Retorno[i++] = (byte)(j);

        }
        return (Retorno);
    }
}
