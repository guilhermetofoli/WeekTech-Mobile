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
import com.weektech.util.SessionManager;
import java.util.ArrayList;

public class PalestrasDiaFragment extends Fragment
        implements PalestraAdapter.OnPalestraClickListener {

    private static final String ARG_DIA = "dia";
    private int dia;
    private RecyclerView    rvPalestras;
    private PalestraAdapter adapter;
    private PalestraDao     palestraDao;
    private InscricaoDao    inscricaoDao;
    private SessionManager  session;

    // padrao do fragment pra passar o dia
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
        return inflater.inflate(R.layout.fragment_palestras_dia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(requireContext());

        // configurando a lista
        rvPalestras = view.findViewById(R.id.rvPalestras);
        rvPalestras.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PalestraAdapter(getContext(), this);
        // passa se é admin pro adapter saber o que mostrar
        adapter.setAdmin(session.isAdmin());
        rvPalestras.setAdapter(adapter);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        palestraDao  = db.palestraDao();
        inscricaoDao = db.inscricaoDao();

        // pega as palestras do dia e atualiza a tela
        palestraDao.listarPorDia(dia).observe(getViewLifecycleOwner(), palestras -> {
            if (palestras == null || palestras.isEmpty()) {
                adapter.setPalestras(new ArrayList<>());
                return;
            }

            // checa o status de inscricao em background
            AppDatabase.databaseExecutor.execute(() -> {
                String raUsuario = session.getRa();

                for (Palestra p : palestras) {
                    boolean inscrito = inscricaoDao.verificarDuplicata(raUsuario, p.id) > 0;
                    if (inscrito) {
                        p.statusInscricao = Palestra.StatusInscricao.INSCRITO;
                    } else if (!p.ativa) {
                        p.statusInscricao = Palestra.StatusInscricao.VISUALIZAR;
                    } else {
                        p.statusInscricao = Palestra.StatusInscricao.DISPONIVEL;
                    }
                }

                // volta pra main thread pra mostrar na lista
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> adapter.setPalestras(palestras));
                }
            });
        });
    }

    @Override
    public void onInscreverClick(Palestra palestra, int position) {
        String ra = session.getRa();
        if (ra.isEmpty()) {
            Toast.makeText(getContext(), "Faça login para se inscrever.", Toast.LENGTH_SHORT).show();
            return;
        }

        // salva a inscricao no banco
        AppDatabase.databaseExecutor.execute(() -> {
            if (inscricaoDao.verificarDuplicata(ra, palestra.id) > 0) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Você já está inscrito nesta palestra.", Toast.LENGTH_SHORT).show());
                }
                return;
            }

            Inscricao inscricao = new Inscricao(ra, palestra.id);
            long id = inscricaoDao.inserir(inscricao);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    if (id > 0) {
                        adapter.atualizarStatus(position, Palestra.StatusInscricao.INSCRITO);
                        Toast.makeText(getContext(), "Inscrição realizada! ✓", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Erro ao se inscrever.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onInscritoClick(Palestra palestra, int position) {
        // se ja ta inscrito abre o checkin
        onCardClick(palestra, position);
    }

    @Override
    public void onVisualizarClick(Palestra palestra, int position) {
        onCardClick(palestra, position);
    }

    @Override
    public void onCardClick(Palestra palestra, int position) {
        // abre a tela de checkin
        Intent intent = new Intent(getContext(), CheckInActivity.class);
        intent.putExtra("PALESTRA_ID",     palestra.id);
        intent.putExtra("PALESTRA_TITULO", palestra.titulo);
        startActivity(intent);
    }

    @Override
    public void onVisualizarPresencasClick(Palestra palestra) {
        // acao de admin pra ver quem confirmou
        Intent intent = new Intent(getContext(), PresencasActivity.class);
        intent.putExtra("palestra_id", palestra.id);
        startActivity(intent);
    }
}
