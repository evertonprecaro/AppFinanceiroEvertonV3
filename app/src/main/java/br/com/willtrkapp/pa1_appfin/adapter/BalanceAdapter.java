package br.com.willtrkapp.pa1_appfin.adapter;
import br.com.willtrkapp.pa1_appfin.R;
import br.com.willtrkapp.pa1_appfin.view.Balance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.AccountBalanceViewHolder> {
    private static List<Balance> account;
    private Context context;
    private static BalanceAdapter.ItemClickListener clickListener;

    public BalanceAdapter(List<Balance> account, Context context) {
        this.account = account;
        this.context = context;
    }

    @Override
    public AccountBalanceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, viewGroup, false);
        return new AccountBalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountBalanceViewHolder contaSaldoViewHolder, int i) {
        contaSaldoViewHolder.description.setText(account.get(i).getDescription());
        contaSaldoViewHolder.balance.setText(String.format("%.2f", account.get(i).getSaldo()));

    }

    @Override
    public int getItemCount() {
        return account.size();
    }

    public void setClickListener(BalanceAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
    public class AccountBalanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView description;
        final TextView balance;

        AccountBalanceViewHolder(View view) {
            super(view);
            description = (TextView)view.findViewById(R.id.descrContaCelula);
            balance = (TextView)view.findViewById(R.id.saldoContaCelula);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }
}
