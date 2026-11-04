package com.example.redsoportex.feature.incidents;

import androidx.annotation.NonNull;

import com.example.redsoportex.data.repository.IncidentRepositoryImpl;
import com.example.redsoportex.data.repository.LendingRepositoryImpl;
import com.example.redsoportex.domain.model.Incident;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.repository.IncidentRepository;
import com.example.redsoportex.domain.repository.LendingRepository;

public final class IncidentFormPresenter implements IncidentFormContract.Presenter {

    private IncidentFormContract.View view;
    private final IncidentRepository incidentRepository;
    private final LendingRepository lendingRepository;
    private Long lendingId;

    public IncidentFormPresenter(IncidentFormContract.View v) {
        this(v, new IncidentRepositoryImpl(), new LendingRepositoryImpl());
    }

    public IncidentFormPresenter(IncidentFormContract.View v,
                                 @NonNull IncidentRepository incidentRepository,
                                 @NonNull LendingRepository lendingRepository) {
        this.view = v;
        this.incidentRepository = incidentRepository;
        this.lendingRepository = lendingRepository;
    }

    @Override
    public void load(Object idArg) {
        if (idArg instanceof Long) {
            this.lendingId = (Long) idArg;
        } else if (idArg instanceof Number) {
            this.lendingId = ((Number) idArg).longValue();
        } else if (idArg instanceof String) {
            try {
                this.lendingId = Long.parseLong((String) idArg);
            } catch (NumberFormatException ignored) {
                this.lendingId = null;
            }
        } else {
            this.lendingId = null;
        }

        if (view == null) return;

        if (this.lendingId == null) {
            view.showLoan("-");
            return;
        }

        lendingRepository.getLendingById(this.lendingId, new LendingRepository.Callback<Lending>() {
            @Override
            public void onSuccess(Lending result) {
                if (view != null) {
                    if (result != null && result.getItemTitle() != null && !result.getItemTitle().isEmpty()) {
                        view.showLoan(result.getItemTitle());
                    } else {
                        view.showLoan("Préstamo #" + lendingId);
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoan("Préstamo #" + lendingId);
                }
            }
        });
    }

    @Override
    public void submit(String type, String description) {
        if (view == null) return;

        boolean typeEmpty = (type == null || type.trim().isEmpty());
        int descError = 0;
        if (description == null || description.trim().isEmpty()) {
            descError = 1;
        } else if (description.trim().length() > 500) {
            descError = 2;
        }

        view.showTypeError(typeEmpty);
        view.showDescriptionError(descError);

        if (!typeEmpty && descError == 0) {
            if (lendingId == null) {
                view.showError("ID de préstamo inválido");
                return;
            }

            String mappedType = mapIncidentType(type);
            view.showLoading(true);
            incidentRepository.reportIncident(lendingId, mappedType, description.trim(), new IncidentRepository.Callback<Incident>() {
                @Override
                public void onSuccess(Incident result) {
                    if (view != null) {
                        view.showLoading(false);
                        view.openReports();
                    }
                }

                @Override
                public void onError(String message) {
                    if (view != null) {
                        view.showLoading(false);
                        view.showError(message);
                    }
                }
            });
        }
    }

    private String mapIncidentType(String type) {
        if (type == null) return "OTHER";
        String normalized = type.trim().toLowerCase();
        if (normalized.contains("daño") || normalized.contains("dano") || normalized.contains("dañado") || normalized.contains("damage")) {
            return "PHYSICAL_DAMAGE";
        } else if (normalized.contains("devuelto") || normalized.contains("devolvi") || normalized.contains("non_return")) {
            return "NON_RETURN";
        } else if (normalized.contains("higiene") || normalized.contains("hygiene")) {
            return "POOR_HYGIENE";
        } else {
            return "OTHER";
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
