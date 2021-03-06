import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientesService } from 'src/app/clientes.service';
import { Cliente } from '../cliente';

@Component({
  selector: 'app-clientes-lista',
  templateUrl: './clientes-lista.component.html',
  styleUrls: ['./clientes-lista.component.css']
})
export class ClientesListaComponent implements OnInit {

  clientes: Cliente[] = [];
  clienteSelecionado: Cliente;
  mensagemSucesso: string;
  mensagemErro: string;

  constructor(
    private service: ClientesService,
    private router: Router
    ) { }

  ngOnInit() {
    this.carregarClientes();
  }

  carregarClientes() {
    this.service.getClientes().subscribe( response => this.clientes = response );
  }

  novoCadastro() {
    this.router.navigate(['/clientes/form']);
  }

  preparaDelecao(cliente: Cliente){
    this.clienteSelecionado = cliente;
  }

  deletarCliente(){
    this.service.deletar(this.clienteSelecionado).subscribe(
      response => {
        this.mensagemSucesso = 'Cliente deletado com sucesso.';
        this.carregarClientes();
      },
      error => this.mensagemErro = 'Ocorreu algum erro ao excluir o cliente. Tente novamente.'
    )
  }

}
