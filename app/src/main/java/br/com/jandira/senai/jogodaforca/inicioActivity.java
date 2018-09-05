package br.com.jandira.senai.jogodaforca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class inicioActivity extends AppCompatActivity {

    TextView txtPalavraJogo;
    TextView txtErros;
    TextView txtAcertos;
    TextView txtCategoria;
    String palavra = "";
    MediaPlayer musica;
    ImageView imgForca;
    char letraEscolha;
    char letraAnt;
    boolean ncMode = false;

    static final  String categorias[] = {
            "ANIMAL",
            "CARRO",
            "SIGNOS",
            "FRUTA"
    };
    static final String[][] palavras = {{ //ANIMAIS
            "GATO",
            "CACHORRO",
            "COELHO",
            "GAIVOTA",
            "ELEFANTE",
            "PANDA",
            "ZEBRA",
            "POMBO",
            "HIPOPOTAMO",
            "COBRA",
            "LONTRA",
            "MACACO"
        },{ //MARCAS DE CARROS
            "VOLKSWAGEN",
            "RENAULT",
            "CHEVROLET",
            "HYUNDAI",
            "HONDA",
            "FORD",
            "FIAT",
            "PEUGEOT",
            "MAZDA",
            "FERRARI",
            "PORSCHE",
            "LAMBORGHINI",
            "AUDI"
        },{ //SIGNOS
            "SAGITARIO",
            "CANCER",
            "LEAO",
            "ARIES",
            "TOURO",
            "PEIXES",
            "LIBRA",
            "CAPRICORNIO",
            "AQUARIO",
            "VIRGEM",
            "GEMEOS",
            "ESCORPIAO"
        },{ //FRUTAS
            "BANANA",
            "LARANJA",
            "MARACUJA",
            "UVA",
            "KIWI",
            "CARAMBOLA",
            "CEREJA",
            "MORANGO",
            "PESSEGO",
            "MANGA",
            "JABUTICABA",
            "AMEIXA"
        }
    };

    static final int VITORIA = 0;
    static final int DERROTA = 1;

    int numeroCategoria = escolherCategoria();
    String categoriaEscolhida = categorias[numeroCategoria];
    String palavraEscolhida = escolherPalavra(numeroCategoria);

    int acertosTotal = palavraEscolhida.length();
    int errosTotal = 6;

    int qtdErros = 0;
    int qtdAcertos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        iniciarMusica();

        txtPalavraJogo = findViewById(R.id.txtPalavraJogo);
        txtErros = findViewById(R.id.txtErros);
        txtAcertos = findViewById(R.id.txtAcertos);
        txtCategoria = findViewById(R.id.txtCategoria);
        imgForca = findViewById(R.id.imgForca);

        txtCategoria.setText(categoriaEscolhida);

        for(int i=0; i <= palavraEscolhida.length() - 1; i++){
            if(i == palavraEscolhida.length() - 1){
                palavra += "_";
            }else{
                palavra += "_ ";
            }
        }
        txtPalavraJogo.setText(palavra);

        atualizarQuantidaDeAcertos();
        atualizarQuantidaDeErros();
    }

    public void acaoBotao(View v) {
        letraEscolha = v.getTag().toString().charAt(0);
        v.setEnabled(false);

        if(letraEscolha == 'C' && letraAnt == 'N'){
            ncMode = true;
        }

        letraAnt = letraEscolha;

        boolean erro = true;

        for(int i = 0; i <= palavraEscolhida.length() - 1; i++){
            if(letraEscolha == palavraEscolhida.charAt(i)){
                StringBuilder novaPalavra = new StringBuilder(palavra);
                novaPalavra.setCharAt(i * 2, letraEscolha);
                palavra = novaPalavra.toString();
                txtPalavraJogo.setText(palavra);
                qtdAcertos += 1;
                erro = false;
            }
        }
        if(erro){
            v.setBackgroundColor(getResources().getColor(R.color.desativadovermelho));
            qtdErros += 1;
            atualizarQuantidaDeErros();
            if(qtdErros == 6){
                txtErros.setTextColor(getResources().getColor(R.color.desativadovermelho));
            }
        }else{
            v.setBackgroundColor(getResources().getColor(R.color.desativadoverde));
            atualizarQuantidaDeAcertos();
            if(qtdAcertos == acertosTotal - 1){
                txtAcertos.setTextColor(getResources().getColor(R.color.desativadoverde));
            }
        }
        atualizarImagem();

        if(qtdErros > errosTotal){
            gameOver(DERROTA);
        }else if(qtdAcertos >= acertosTotal){
            gameOver(VITORIA);
        }
    }

    public void reiniciar(View v){
        pararMusica();
        recreate();
    }

    private void gameOver(int condicaoVitoria){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        pararMusica();

        if(condicaoVitoria == VITORIA){
            alert.setTitle("Você Ganhou \uD83D\uDE00");
            alert.setMessage("Você acertou a palavra. Parabéns.");
        }else{
            alert.setTitle("Você Perdeu \uD83D\uDE22");
            alert.setMessage("Você errou a palavra. A palavra é '" + palavraEscolhida + "'.");
        }
        alert.setNegativeButton("sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setPositiveButton("reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });

        alert.create().show();
    }

    private void atualizarQuantidaDeAcertos(){
        txtAcertos.setText("Acertos: " + qtdAcertos);
    }

    private void atualizarQuantidaDeErros(){
        txtErros.setText("Erros: " + qtdErros + "/" + errosTotal);
    }


    private int escolherCategoria(){
        int min = 0;
        int max = categorias.length - 1;

        return new Random().nextInt((max - min) + 1) + min;
    }
    private String escolherPalavra(int numeroCategoria){
        int min = 0;
        int max = palavras[numeroCategoria].length - 1;
        final int numeroPalavra = new Random().nextInt((max - min) + 1) + min;

        return palavras[numeroCategoria][numeroPalavra];
    }

    @Override
    public void onBackPressed() {
        pararMusica();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void atualizarImagem(){
        if(ncMode){
            if(qtdErros == 1){
                imgForca.setImageResource(R.drawable.nc1);
            }else if(qtdErros == 2){
                imgForca.setImageResource(R.drawable.nc2);
            }else if(qtdErros == 3){
                imgForca.setImageResource(R.drawable.nc3);
            }else if(qtdErros == 4){
                imgForca.setImageResource(R.drawable.nc4);
            }else if(qtdErros == 5){
                imgForca.setImageResource(R.drawable.nc5);
            }else if(qtdErros == 6){
                imgForca.setImageResource(R.drawable.nctotal);
            }
        }else{
            if(qtdErros == 1){
                imgForca.setImageResource(R.drawable.hm1);
            }else if(qtdErros == 2){
                imgForca.setImageResource(R.drawable.hm2);
            }else if(qtdErros == 3){
                imgForca.setImageResource(R.drawable.hm3);
            }else if(qtdErros == 4){
                imgForca.setImageResource(R.drawable.hm4);
            }else if(qtdErros == 5){
                imgForca.setImageResource(R.drawable.hm5);
            }else if(qtdErros == 6){
                imgForca.setImageResource(R.drawable.hmtotal);
            }
        }
    }

    public void iniciarMusica(){
        musica = MediaPlayer.create(getApplicationContext(),R.raw.jogo);
        musica.start();
    }

    public void pararMusica(){
        musica.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pararMusica();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musica.setLooping(true);
        musica.start();
    }
}