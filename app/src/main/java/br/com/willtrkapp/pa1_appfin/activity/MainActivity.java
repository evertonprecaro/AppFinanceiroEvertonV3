package br.com.willtrkapp.pa1_appfin.activity;
import br.com.willtrkapp.pa1_appfin.R;
import br.com.willtrkapp.pa1_appfin.adapter.BalanceAdapter;
import br.com.willtrkapp.pa1_appfin.data.AccountData;
import br.com.willtrkapp.pa1_appfin.model.Account;
import br.com.willtrkapp.pa1_appfin.view.Balance;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private AccountData accountData;
    private RecyclerView recyclerView;
    private TextView textViewSaldoAtual;
    private List<Balance> accounts = new ArrayList<>();
    private BalanceAdapter adapter;
    private FloatingActionMenu floatingMenu;
    private FloatingActionButton floatingActionAccount, floatingActionDebt, floatingActionCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        handleIntent(intent);



        accountData = new AccountData(this);



        textViewSaldoAtual = (TextView) findViewById(R.id.textViewSaldoAtualContas);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.contas_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new BalanceAdapter(accounts, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();
        floatingActionAccount = (FloatingActionButton) findViewById(R.id.fabNvConta);
        floatingActionCredit = (FloatingActionButton) findViewById(R.id.fabNvCredito);
        floatingActionDebt = (FloatingActionButton) findViewById(R.id.fabNvDespesa);
        floatingMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        floatingMenu.setClosedOnTouchOutside(true);
        startEventsOnFloatingMenu();
        updateUI(); }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) { } }

    //Para ter controle dos clicks individuais de cada botão
        private void startEventsOnFloatingMenu() {
        floatingActionAccount.setOnClickListener(onFloatButtonClick());
        floatingActionDebt.setOnClickListener(onFloatButtonClick());
        floatingActionCredit.setOnClickListener(onFloatButtonClick());
    }

    private View.OnClickListener onFloatButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == floatingActionAccount) {
                    Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivityForResult(i, 1);
                } else if (view == floatingActionDebt) {
                    Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                    i.setAction("Efetuar Débito");
                    startActivityForResult(i, 3);
                } else if (view == floatingActionCredit) {
                    Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                    i.setAction("Efetuar Crédito");
                    startActivityForResult(i, 3);
                }
                floatingMenu.close(true);
            }
        };
    }

    private void setupRecyclerView() {

        adapter.setClickListener(new BalanceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

                final Account conta = accounts.get(position);
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                i.putExtra("conta", conta);
                startActivityForResult(i, 2);
            }
        });




        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Account conta = accounts.get(viewHolder.getAdapterPosition());
                    accountData.removeAccount(conta);
                    accounts.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                    updateUI();
                    showToast(getResources().getString(R.string.conta_removida));

                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }



        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                showToast(getResources().getString(R.string.conta_adicionada));
                updateUI(); }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK)
                showToast(getResources().getString(R.string.alteracoes_realizadas));
            if (resultCode == 3)
                showToast(getResources().getString(R.string.conta_removida));
            updateUI(); }

        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                showToast(data.getAction());
                updateUI(); }
        }
    }

    private void updateUI() {
        float saldoAtualContas = accountData.getAccountBalance();
        textViewSaldoAtual.setText("Situação Atual R$ " + saldoAtualContas);

        accounts.clear();
        accounts.addAll(accountData.allAccountsPlusBalance());

        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount()==0)
            /*empty.setVisibility(View.VISIBLE);*/
            showToast(getResources().getString(R.string.lista_contas_vazia)); }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
