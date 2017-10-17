package app.suleymancan.gaziantepsports;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //tanımlama (initializing islemleri)
    String data="";
    final String mimeType="text/html; charset=UTF-8";
    final String encoding="utf-8";
    String cekilecekUrl;
    String cekilecekKisim;
    String removeKisim;
    WebView webView;
    ProgressDialog progressDialog=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar name gizleme
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        WebViewHiz();
        //acilis ekrani
        AcilisEkrani();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //kendimce webview hız
    public void WebViewHiz(){

        //webView hızlandırma, tanımlama islemleri vs.
        webView=(WebView)findViewById(R.id.WebView1);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSaveFormData(true);


    }
    //üst menü
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.geri){
            if (webView.canGoBack()){
                webView.goBack();
            }
            else {
                Toast.makeText(MainActivity.this,"Ana sayfadasın rafık.",Toast.LENGTH_SHORT).show();
            }



        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.anasayfa) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        if (id==R.id.cikis){
            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Çıkıyor musun rafık?");
            builder.setCancelable(true);
            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"Görüşmek üzere rafık :)",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();


        }


        return super.onOptionsItemSelected(item);
    }

    //acilis ekrani
    public void AcilisEkrani()
    {
        cekilecekUrl="https://suleymancanblog.com/2017/01/01/gaziantep-sports/";
        cekilecekKisim="div.entry-content";
        removeKisim="bos";
        data="";
        webView.setWebViewClient(new CekWebViewClient());
        new VeriCek().execute();
    }
    //yan menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id==R.id.ozet){
            cekilecekUrl="https://suleymancanblog.com/2017/01/01/ozet/";
            cekilecekKisim="div.entry-content";
            removeKisim="bos";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        } else if (id == R.id.f_puandurumu) {
            cekilecekUrl="http://www.amatorfutbol.org/tff/ulusal-turkiye-sampiyonlugu/puandurumu-4761.html";
            cekilecekKisim="div.table-responsive";
            removeKisim="td.hucre";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        } else if (id == R.id.gaziantepspor) {
            cekilecekUrl="https://tr.0wikipedia.org/index.php?q=aHR0cHM6Ly90ci53aWtpcGVkaWEub3JnL3dpa2kvR2F6aWFudGVwc3Bvcl8yMDE3LTE4X3Nlem9udQ";
            cekilecekKisim="table.collapsible.collapsed";
            removeKisim="a.external.text,th,td:eq(4)";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        } else if (id == R.id.gazisehir) {
            cekilecekUrl="https://tr.0wikipedia.org/index.php?q=aHR0cHM6Ly90ci53aWtpcGVkaWEub3JnL3dpa2kvR2F6aSVDNSU5RmVoaXJfR2F6aWFudGVwX0ZLXzIwMTctMThfc2V6b251";
            cekilecekKisim="table.collapsible.collapsed";
            removeKisim="a.external.text,th,td:eq(4)";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();

        } else if (id == R.id.gaziantepspor_kadro) {
            cekilecekUrl="https://suleymancanblog.com/2017/01/01/gaziantepspor-kadro-gaziantepsportsapp/";
            cekilecekKisim="div.entry-content";
            removeKisim="bos";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        } else if (id == R.id.gazisehir_kadro) {
            cekilecekUrl="https://suleymancanblog.com/2017/01/01/gazisehir-kadro-gaziantepsportsapp/";
            cekilecekKisim="div.entry-content";
            removeKisim="bos";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        }
        else if (id == R.id.gaziantepbasketbol) {
            cekilecekUrl="http://www.bsl.org.tr/bsl/istatistikler/takim-detay?Sezon=161&Lig=8983&KulupKodu=75329";
            cekilecekKisim="div.panel.panel-default";
            removeKisim="bos";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        }
        else if(id==R.id.b_puandurumu){
            cekilecekUrl="http://www.bsl.org.tr/bsl/istatistikler/puan-durumu";
            cekilecekKisim="div#contentPlaceholder_C038_Col00.sf_colsIn.sf_1col_1in_100";
            removeKisim="div.anasayfareklam";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        } else if (id == R.id.gaziantep27) {
            cekilecekUrl="http://www.gaziantep27.net/spor-haberleri-3hk.htm";
            cekilecekKisim="main";
            removeKisim="div.page-title,div.bar-lm,aside.base-side.clearfix,div.headline.fit-main,div.holder,ul.clearfix,div.b";
            data="";
            webView.setWebViewClient(new CekilmeyenWebClient());
            new VeriCek().execute();
        }
        else if (id == R.id.gunes) {

            cekilecekUrl="http://www.gaziantepgunes.com/content.asp?haber=22265976";
            cekilecekKisim="div.yatayhaberler";
            removeKisim="a.btn";
            data="";
            webView.setWebViewClient(new CekilmeyenWebClient());
            new VeriCek().execute();
            webView.loadData(data, mimeType, encoding);
            //dikkat
        }
        else if (id == R.id.olaymedya) {
            webView.loadUrl("http://www.olaymedya.com/kategori/spor");
            webView.setWebViewClient(new CekilmeyenWebClient());
        }
        else if (id == R.id.pusula) {
            webView.loadUrl("http://www.gazianteppusula.com/kategori/spor-haberleri-8.html");
            webView.setWebViewClient(new CekilmeyenWebClient());
        }
        else if (id == R.id.telgraf) {
            webView.loadUrl("http://telgraf.net/kategori/spor-haberleri-7.html");
            webView.setWebViewClient(new CekilmeyenWebClient());
        }
        else if (id == R.id.hakkimizda) {
            cekilecekUrl="https://suleymancanblog.com/2017/01/01/gaziantepsportsapp-hakkimizda/";
            cekilecekKisim="div.entry-content";
            removeKisim="bos";
            data="";
            webView.setWebViewClient(new CekWebViewClient());
            new VeriCek().execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //geri dügmesine basıldığında...
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()){
                        webView.goBack();
                    }
                    else {
                        finish();
                    }
                    return  true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
    private class VeriCek extends AsyncTask<Void,Void,String>
    {
        //arka planda calisacak islem
        //sitenin ilgili kısmını jsoup kütüphanesi yardımıyla cekme.
        @Override
        protected String doInBackground(Void... params) {
            try {
                Document document= Jsoup.connect(cekilecekUrl).data("query", "Java")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(3000)
                        .post();
                Element style=document.head();
                Elements elements=document.select(cekilecekKisim);
                document.select(removeKisim).remove();
                data+=style;
                Element element;
                for(int i=0; i<elements.size();i++)
                {
                    element=elements.get(i);
                    data+=element.outerHtml();
                }
                return data;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "Hata var rafık. İnternet bağlantını kontrol et.";
        }
        //arka plan islemi bittikten sonra...

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            webView.loadData(s,mimeType,encoding);
        }
    }
    private class CekWebViewClient extends WebViewClient{
        //webview uzerinde tıklamayı kapatma.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals(cekilecekUrl)) {
                view.loadData(data,mimeType,encoding);
            }
            return true;
        }
        //yükleniyor...
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialog=new ProgressDialog(MainActivity.this,R.style.ProgressDialogTema);
            progressDialog.setTitle("Lütfen bekle rafık");
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.show();
            // android.view.WindowManager$BadTokenException:
            // cozüm olarak:
            if(!isFinishing()){
                progressDialog.show();}
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialog.dismiss();
            super.onPageFinished(view, url);
        }
    }
    private class CekilmeyenWebClient extends WebViewClient{

        //yükleniyor...
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialog=new ProgressDialog(MainActivity.this,R.style.ProgressDialogTema);
            progressDialog.setTitle("Lütfen bekle rafık");
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.show();
            // android.view.WindowManager$BadTokenException:
            // cozüm olarak:
            if(!isFinishing()){
            progressDialog.show();}
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialog.dismiss();
            super.onPageFinished(view, url);
        }


    }
}
