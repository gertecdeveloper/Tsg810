package com.example.gondolapriceview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.ftpos.library.smartpos.printer.OnPrinterCallback;
import com.ftpos.library.smartpos.printer.PrintStatus;
import com.ftpos.library.smartpos.printer.Printer;

public class FTPrntr
{
    Printer mPrinter;
    PrintStatus mStatus;
    Context mContext;

    public FTPrntr()
    {

    }

    public FTPrntr(Context context)
    {
        mContext = context;
    }
    public com.ftpos.library.smartpos.printer.Printer getInstance(Context context)
    {
        mContext = context;
        mStatus =  new PrintStatus();
        mPrinter = Printer.getInstance(mContext);
        return mPrinter;
    }

    public int open()
    {
        return mPrinter.open();
    }

    public int startCaching()
    {
        return mPrinter.startCaching();
    }


    public int getStatus()
    {
        int iRet = -1;

        if( mPrinter != null ) {
            iRet = mPrinter.getStatus(mStatus);
            Log.e("FTPrntr","iRet:"+iRet);
        }
        return iRet;
    }

    public boolean isHavePaper()
    {
        boolean bRet = true;
        boolean bHavePaper =mStatus.getmIsHavePaper();
        if(!bHavePaper) {
            //setTextOnUiTread("Printer no paper!");
            Log.d("TAG","Printer no paper!");
            bRet = false;
        }
        Log.d("TAG","Printer has paper!");
        return bRet;
    }

    public int getUsedPaperLenManage()
    {
        if( mPrinter != null) {
            return mPrinter.getUsedPaperLenManage();
        }
        return 0;
    }

    public int getTemperature()
    {
        return mStatus.getmTemperature();
    }


    public int setGray(int level)
    {
        return mPrinter.setGray(level);
    }

    public void setSpace(int linespacing, int columnspacing) {
        mPrinter.setSpace(linespacing,columnspacing);
    }

    public void setMargin(int left, int right) {
        mPrinter.setMargin(left,right);
    }

    public void setFont(Bundle bundle) {
        mPrinter.setFont(bundle);
    }

    public void printStr(String s) {
        mPrinter.printStr(s);
    }

    public int printBmp(Bitmap image)
    {
        return  mPrinter.printBmp(image);
    }

    public void setAlignStyle(int style)
    {
        mPrinter.setAlignStyle(style);
    }

    public void print()
    {
        mPrinter.print(new OnPrinterCallback() {
            @Override
            public void onSuccess() {
                //setTextOnUiTread("print success  ");
                Log.e("PRNT","print success");
            }

            @Override
            public void onError(int errCode) {
                //setTextOnUiTread("Print  error：0x"+int2HexStr(errCode));
                Log.e("PRNT","Print error");
            }
        });
    }

    public int QrCode(byte[] qrdata, int mode){
        return mPrinter.printQRCode(qrdata, mode);
    }
}

