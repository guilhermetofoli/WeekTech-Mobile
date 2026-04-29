
package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weektech.adapter.PalestraAdapter;
import com.weektech.database.AppDatabase;
import com.weektech.database.dao.InscricaoDao;
import com.weektech.database.dao.PalestraDao;
import com.weektech.model.Palestra;

import java.util.ArrayList;
import java.util.List;

public class PalestrasDiaFragment extends Fragment
        implements PalestraAdapter.OnPalestraClickListener {

    // Chave do argumento
    private static final String ARG_DIA = "dia";
    private int dia; // 1, 2 ou 3
    private RecyclerView    rvPalestras;
    private PalestraAdapter adapter;
    private PalestraDao     palestraDao;
    private InscricaoDao    inscricaoDao;

    // Factory: cria o fragment passando o número do dia
    public static PalestrasDiaFragment newInstance(int dia) {
        PalestrasDiaFragment fragment = new PalestrasDiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DIA, dia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dia = getArguments().getInt(ARG_DIA, 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla o fragment_palestras_dia.xml (apenas o RecyclerView)
        return inflater.inflate(R.layout.fragment_palestras_dia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuração do RecyclerView
        rvPalestras = view.findViewById(R.id.rvPalestras);
        rvPalestras.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PalestraAdapter(getContext(), this);
        rvPalestras.setAdapter(adapter);

        // Acesso ao banco Room
        AppDatabase db = AppDatabase.getInstance(requireContext());
        palestraDao  = db.palestraDao();
        inscricaoDao = db.inscricaoDao();

        // Observa as palestras do dia via LiveData
        // Sempre que o banco mudar, a UI é atualizada automaticamente
        palestraDao.listarPorDia(dia).observe(getViewLifecycleOwner(), palestras -> {
            if (palestras == null || palestras.isEmpty()) {
                adapter.setPalestras(new ArrayList<>());
                return;
            }

            // Para cada palestra, verifica se o usuario está inscrito
            AppDatabase.databaseExecutor.execute(() -> {
                // RA do usuário logado (substitua pela lógica real de sessão)
                String raUsuario = obterRaUsuarioLogado();

                for (Palestra p : palestras) {
                    boolean inscrito = inscricaoDao
                            .verificarDuplicata(raUsuario, p.id) > 0;

                    // Define o status visual do botão
                    if (inscrito) {
                        p.statusInscricao = Palestra.StatusInscricao.INSCRITO;
                    } else if (!p.ativa) {
                        // Palestra encerrada → só pode visualizar
                        p.statusInscricao = Palestra.StatusInscricao.VISUALIZAR;
                    } else {
                        p.statusInscricao = Palestra.StatusInscricao.DISPONIVEL;
                    }
                }

                // Atualiza o adapter na main thread
                requireActivity().runOnUiThread(() ->
                        adapter.setPalestras(palestras));
            });
        });
    }

    @Override
    public void onInscreverClick(Palestra palestra, int position) {
        Intent intent = new Intent(getContext(), InscricaoActivity.class);
        intent.putExtra("PALESTRA_ID",     palestra.id);
        intent.putExtra("PALESTRA_TITULO", palestra.titulo);
        startActivity(intent);
    }

    @Override
    public void onInscritoClick(Palestra palestra, int position) {
        Toast.makeText(getContext(),
                "Você já está inscrito em: " + palestra.titulo,
                Toast.LENGTH_SHORT).show();
        // Opcional: exibir AlertDialog para cancelar inscrição
    }

    // usuario clicou em "Visualizar" → abre detalhes (somente leitura)
    @Override
    public void onVisualizarClick(Palestra palestra, int position) {
        Intent intent = new Intent(getContext(), PalestraDetailActivity.class);
        intent.putExtra("PALESTRA_ID", palestra.id);
        startActivity(intent);
    }

    // usuario clicou no card → abre detalhes
    @Override
    public void onCardClick(Palestra palestra, int position) {
        Intent intent = new Intent(getContext(), PalestraDetailActivity.class);
        intent.putExtra("PALESTRA_ID", palestra.id);
        startActivity(intent);
    }

    private String obterRaUsuarioLogado() {
        return requireContext()
                .getSharedPreferences("techweek_prefs", 0)
                .getString("ra_usuario", "");
    }
}