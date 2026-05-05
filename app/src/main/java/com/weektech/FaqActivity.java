package com.weektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        RecyclerView rvFaq = findViewById(R.id.rvFaq);
        rvFaq.setLayoutManager(new LinearLayoutManager(this));

        List<FaqItem> faqItems = Arrays.asList(
                new FaqItem("O que é a Tech Week?",
                        "A Tech Week é a semana de tecnologia da UniCesumar, com palestras, projetos e atividades sobre tecnologia e inovação."),
                new FaqItem("Como me inscrever em uma palestra?",
                        "Faça login no app, acesse a aba 'Cronograma', escolha o dia desejado e toque em 'Inscrever-se' na palestra de seu interesse."),
                new FaqItem("Posso me inscrever em mais de uma palestra?",
                        "Sim! Você pode se inscrever em quantas palestras quiser, desde que os horários não se conflitem."),
                new FaqItem("Como confirmar minha presença?",
                        "Ao final de cada palestra, toque no card da palestra e clique em 'Confirmar Presença'. Você precisa estar presente no campus da UniCesumar."),
                new FaqItem("O que é o Coffee Break?",
                        "O Coffee Break é um momento de networking durante o evento. Marque a opção ao se cadastrar para confirmar sua participação."),
                new FaqItem("Como apresentar um projeto?",
                        "Acesse a aba 'Projetos' no app e preencha o formulário com os dados do seu projeto (Nome, RA, Nome do Projeto e Descrição)."),
                new FaqItem("Onde será realizado o evento?",
                        "O evento acontece na UniCesumar Londrina. Confira o endereço completo no Google Maps pelo link disponível no app."),
                new FaqItem("Esqueci minha senha, o que faço?",
                        "No momento, entre em contato com a organização do evento. Em breve teremos a opção de recuperação de senha pelo e-mail cadastrado."),
                new FaqItem("O app funciona sem internet?",
                        "Sim! Os dados são armazenados localmente no seu dispositivo via banco de dados Room, então você pode visualizar o cronograma offline.")
        );

        rvFaq.setAdapter(new FaqAdapter(faqItems));
    }

    // ─── Model ───────────────────────────────────────────────
    static class FaqItem {
        String pergunta, resposta;
        boolean expandido = false;

        FaqItem(String pergunta, String resposta) {
            this.pergunta = pergunta;
            this.resposta = resposta;
        }
    }

    // ─── Adapter ─────────────────────────────────────────────
    static class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.VH> {

        private final List<FaqItem> items;

        FaqAdapter(List<FaqItem> items) { this.items = items; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            FaqItem item = items.get(position);
            holder.tvPergunta.setText("❓  " + item.pergunta);
            holder.tvResposta.setText(item.resposta);
            holder.tvResposta.setVisibility(item.expandido ? View.VISIBLE : View.GONE);

            holder.tvPergunta.setOnClickListener(v -> {
                item.expandido = !item.expandido;
                notifyItemChanged(position);
            });
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvPergunta, tvResposta;
            VH(@NonNull View v) {
                super(v);
                tvPergunta = v.findViewById(android.R.id.text1);
                tvResposta = v.findViewById(android.R.id.text2);
                tvPergunta.setTextSize(15f);
                tvPergunta.setPadding(16, 20, 16, 16);
                tvResposta.setTextSize(13f);
                tvResposta.setPadding(32, 0, 16, 20);
            }
        }
    }
}
