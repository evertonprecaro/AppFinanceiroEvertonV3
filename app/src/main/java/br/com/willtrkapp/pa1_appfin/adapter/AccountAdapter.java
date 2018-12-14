package br.com.willtrkapp.pa1_appfin.adapter;
import java.util.List;
import br.com.willtrkapp.pa1_appfin.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.willtrkapp.pa1_appfin.model.Account;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private static List<Account> accounts;
    private Context context;
    private static ItemClickListener clickListener;

    public AccountAdapter(List<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, viewGroup, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder accountViewHolder, int i) {
        accountViewHolder.description.setText(accounts.get(i).getDescription());
        accountViewHolder.balance.setText(String.format("%.2f", accounts.get(i).getStartBalance()));

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
    public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView description;
        final TextView balance;

        AccountViewHolder(View view) {
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
