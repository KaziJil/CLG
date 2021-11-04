package com.example.collageadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;

public class UploadPDFActivity extends AppCompatActivity {
    private CardView cardView;
    private final int REQ=1;
    private Uri pdfData;
    private EditText title;
    private Button pdf_button,previues_page,next_page;
    //uploadFirebaseImage
    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog dialog;
    TextView page_number;
    ImageView pdf_show;
    String pdfName;
    int total_page=0;
    int displayPage=0;
    public static final int PICK_FILE=99;
    PdfRenderer renderer;
    TextView pdfNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdfactivity);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        dialog=new ProgressDialog(UploadPDFActivity.this);
        cardView=findViewById(R.id.selectPDF);
        title=findViewById(R.id.setTitlePDF);
        pdf_button=findViewById(R.id.PDF_button);
        page_number=findViewById(R.id.pageNumber);
        pdfNameText=findViewById(R.id.pdfNameTextView);
        pdf_show=findViewById(R.id.pdfImageView);
        previues_page=findViewById(R.id.pdf_preview);
        next_page=findViewById(R.id.pdf_nextPage);
//cardView where we click for pdf
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        //this is for previus pageNumber when click it pdf back in one page
        previues_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayPage > 0){
                    displayPage--;
                    displayMethods(displayPage);
                }
            }
        });
        //this is for next pageNumber when click next button pdf goto the next page if pdf has multiple pages
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayPage<(total_page-1)){
                    displayPage++;
                    displayMethods(displayPage);
                }
            }
        });
    }
    // this is for displaying the pdf in imageView
    private void displayMethods(int _number) {
        if (renderer!=null){
            PdfRenderer.Page page=renderer.openPage(_number);
            Bitmap bitmap=Bitmap.createBitmap(page.getWidth(),page.getHeight(),Bitmap.Config.ARGB_8888);
            page.render(bitmap,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pdf_show.setImageBitmap(bitmap);
            page.close();
            page_number.setText((_number+1) + "/" + total_page);
        }

    }
//this is for methods for the pick pdf in gallery
    private void openGallery() {
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,PICK_FILE);
    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_FILE && resultCode==RESULT_OK){
            if (data!=null){
                Uri uri=data.getData();
                pdfData=data.getData();
                if (pdfData.toString().startsWith("content://")){
                    Cursor cursor=null;
                    cursor=UploadPDFActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                    if (cursor!=null && cursor.moveToFirst()){
                        pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    }

                }else if (pdfData.toString().startsWith("file://")){
                    pdfName=new File(pdfData.toString()).getName();
                }
                try {
                    ParcelFileDescriptor parcelFileDescriptor=getContentResolver().openFileDescriptor(uri,"r");
                    renderer=new PdfRenderer(parcelFileDescriptor);
                    total_page=renderer.getPageCount();
                    displayPage=0;
                    displayMethods(displayPage);
                }catch (FileNotFoundException file){

                }catch (IOException e){

                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (renderer!=null){
            renderer.close();
        }
    }

}