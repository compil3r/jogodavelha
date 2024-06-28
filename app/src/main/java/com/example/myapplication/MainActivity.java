package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button[][] botoes = new Button[3][3]; // Matriz de botões para representar a grade do jogo
    private boolean vezDoJogador = true; // true para jogador, false para máquina
    private int contadorRodadas = 0; // Contador de rodadas

    private int pontuacaoJogador = 0; // Pontuação do jogador
    private int pontuacaoMaquina = 0; // Pontuação da máquina

    private TextView textViewPontuacaoJogador; // Exibe a pontuação do jogador
    private TextView textViewPontuacaoMaquina; // Exibe a pontuação da máquina
    private TextView textViewResultado; // Exibe o resultado do jogo (vitória, derrota ou empate)
    private Button buttonReiniciar; // Botão para reiniciar o jogo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando os elementos da interface
        textViewResultado = findViewById(R.id.textViewResultado);
        textViewPontuacaoJogador = findViewById(R.id.textViewPontuacaoJogador);
        textViewPontuacaoMaquina = findViewById(R.id.textViewPontuacaoMaquina);
        buttonReiniciar = findViewById(R.id.buttonReiniciar);

        // Obter referências aos botões diretamente
        botoes[0][0] = findViewById(R.id.button00);
        botoes[0][1] = findViewById(R.id.button01);
        botoes[0][2] = findViewById(R.id.button02);
        botoes[1][0] = findViewById(R.id.button10);
        botoes[1][1] = findViewById(R.id.button11);
        botoes[1][2] = findViewById(R.id.button12);
        botoes[2][0] = findViewById(R.id.button20);
        botoes[2][1] = findViewById(R.id.button21);
        botoes[2][2] = findViewById(R.id.button22);

        // Configurando os ouvintes de clique para os botões
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isEnabled()) {
                            return; // Se o botão já foi clicado, não faz nada
                        }

                        if (vezDoJogador) {
                            ((Button) v).setText("O"); // Jogada do jogador
                        } else {
                            ((Button) v).setText("X"); // Jogada da máquina
                        }

                        v.setEnabled(false); // Desabilita o botão para que não possa ser clicado novamente

                        contadorRodadas++;

                        if (verificarVitoria()) { // Verifica se há um vencedor
                            if (vezDoJogador) {
                                pontuacaoJogador++;
                                atualizarPontuacao();
                                textViewResultado.setText("Jogador vence!");
                            } else {
                                pontuacaoMaquina++;
                                atualizarPontuacao();
                                textViewResultado.setText("Máquina vence!");
                            }
                            buttonReiniciar.setEnabled(true); // Habilita o botão de reinício
                        } else if (contadorRodadas == 9) { // Verifica se deu empate
                            textViewResultado.setText("Empate!");
                            buttonReiniciar.setEnabled(true); // Habilita o botão de reinício
                        } else {
                            vezDoJogador = !vezDoJogador; // Alterna a vez

                            if (!vezDoJogador) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        jogadaMaquina(); // Jogada da máquina com delay
                                    }
                                }, 500); // Delay de 500ms para a jogada da máquina
                            }
                        }
                    }
                });
            }
        }
    }

    private void jogadaMaquina() {
        Random random = new Random();
        int i, j;

        // Encontra uma posição aleatória disponível
        do {
            i = random.nextInt(3);
            j = random.nextInt(3);
        } while (!botoes[i][j].isEnabled());

        botoes[i][j].setText("X");
        botoes[i][j].setEnabled(false); // Desabilita o botão para que não possa ser clicado novamente
        contadorRodadas++;

        if (verificarVitoria()) { // Verifica se a máquina venceu
            pontuacaoMaquina++;
            atualizarPontuacao();
            textViewResultado.setText("Máquina vence!");
            buttonReiniciar.setEnabled(true); // Habilita o botão de reinício
        } else if (contadorRodadas == 9) { // Verifica se deu empate
            textViewResultado.setText("Empate!");
            buttonReiniciar.setEnabled(true); // Habilita o botão de reinício
        } else {
            vezDoJogador = !vezDoJogador; // Alterna a vez
        }
    }

    private boolean verificarVitoria() {
        String[][] campo = new String[3][3];

        // Preenche o array campo com os valores dos botões
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                campo[i][j] = botoes[i][j].getText().toString();
            }
        }

        // Verifica linhas
        for (int i = 0; i < 3; i++) {
            if (campo[i][0].equals(campo[i][1]) && campo[i][0].equals(campo[i][2]) && !campo[i][0].equals("")) {
                return true;
            }
        }

        // Verifica colunas
        for (int i = 0; i < 3; i++) {
            if (campo[0][i].equals(campo[1][i]) && campo[0][i].equals(campo[2][i]) && !campo[0][i].equals("")) {
                return true;
            }
        }

        // Verifica diagonais
        if (campo[0][0].equals(campo[1][1]) && campo[0][0].equals(campo[2][2]) && !campo[0][0].equals("")) {
            return true;
        }

        if (campo[0][2].equals(campo[1][1]) && campo[0][2].equals(campo[2][0]) && !campo[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void atualizarPontuacao() {
        textViewPontuacaoJogador.setText("Jogador: " + pontuacaoJogador);
        textViewPontuacaoMaquina.setText("Máquina: " + pontuacaoMaquina);
    }

    private void reiniciarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j].setText("");
                botoes[i][j].setEnabled(true); // Habilita novamente os botões para o novo jogo
            }
        }
        contadorRodadas = 0;
        vezDoJogador = true;
        textViewResultado.setText("");
    }

    public void reiniciarJogo(View view) {
        reiniciarTabuleiro();
        buttonReiniciar.setEnabled(false); // Desabilita o botão de reinício até que o jogo termine novamente
    }
}