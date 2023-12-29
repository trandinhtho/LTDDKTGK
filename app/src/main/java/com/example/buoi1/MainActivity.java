package com.example.buoi1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buoi1.DAO.ProductDAO;
import com.example.buoi1.adapter.ProductAdapter;
import com.example.buoi1.adapter.SwipeItem;
import com.example.buoi1.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnDeleteClickListener {

    private RecyclerView rcvProduct;
    private ProductAdapter adapter;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvProduct = findViewById(R.id.rcvProduct);
        productDAO = new ProductDAO(this);

        List<Product> listProduct = productDAO.GetAll();

        adapter = new ProductAdapter(listProduct, this, this);
        rcvProduct.setAdapter(adapter);
        rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItem(adapter));
        itemTouchHelper.attachToRecyclerView(rcvProduct);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(itemDecoration);

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog();
            }
        });
    }

    private void showAddProductDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(viewDialog.getContext());
        builder.setView(viewDialog);
        AlertDialog alert = builder.create();
        alert.show();

        EditText txtName = viewDialog.findViewById(R.id.edtName);
        EditText txtImage = viewDialog.findViewById(R.id.edtImage);
        EditText txtPrice = viewDialog.findViewById(R.id.edtPrice);

        Button btnClear = viewDialog.findViewById(R.id.btnClear);
        Button btnSaveProduct = viewDialog.findViewById(R.id.btnDialogSaveProduct);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the EditText fields
                txtName.getText().clear();
                txtImage.getText().clear();
                txtPrice.getText().clear();
            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product p = new Product(txtName.getText().toString(), Float.parseFloat(txtPrice.getText().toString()), txtImage.getText().toString());
                productDAO.add(p);
                refreshProductList();
                Toast.makeText(viewDialog.getContext(), "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        showDeleteConfirmationDialog(position);
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing if the user clicks "No"
            }
        });
        builder.show();
    }

    private void deleteProduct(int position) {
        productDAO.Delete(adapter.getListProduct().get(position).getId());
        refreshProductList();
        Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
    }

    private void refreshProductList() {
        adapter.refreshList(productDAO.GetAll());
    }
}
