package sintatico;

public class Sintatico {
    private Lexico lexico;
    private Token token;
    private int linha;
    private int coluna;


    public Sintatico(String nomeArquivo){
        linha = 1;
        coluna = 1;
        this.lexico = new Lexico(nomeArquivo);
    }

    public void analisar(){
        lerToken();
        programa();
    }

    public void lerToken(){
        token = lexico.getToken(linha, coluna);
		System.out.println(token);
        coluna = token.getColuna()+token.getTamanhoToken();
        linha = token.getLinha();
    }

    //<programa> ::= program <id> {A1} <corpo> • {A30}
    public void programa(){
        if(token.getClasse() == Classe.cPalRes &&
            token.getValor().getValorIdentificador().equalsIgnoreCase("program")){
                lerToken();
                if(token.getClasse() == Classe.cId){ //id()
				lerToken();
                //{A1}
                corpo();
                if(token.getClasse() == Classe.cPonto){
                    lerToken();
                    //{A30}
                }else{
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou um ponto final no program");
                }
                }else{
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o identificador do program");
                }
            }else{
                    System.out.println("Faltou começar por PROGRAM");
            }
    }

    //<corpo> ::= <declara> <rotina> begin <sentencas> end
    public void corpo(){
        declara();
        //rotina(); NÃO É PRA FAZER
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))) {
			lerToken();
			sentencas();
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))) {
				lerToken();
			}else {
				System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o end no fim do programa");
			}
		}else {
            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o begin no corpo do programa");
		}
    }

    //<declara> ::= var <dvar> <mais_dc> | 
    public void declara(){
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("var"))) {
			lerToken();
			dvar();
			mais_dc();
		}
    }

    //<mais_dc> ::=  ; <cont_dc>
    public void mais_dc() {
		if (token.getClasse() == Classe.cPontoVirgula) {
			lerToken();
			cont_dc();			
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou colocar o ; no mais_dc");
		}
	}

    //<cont_dc> ::= <dvar> <mais_dc> | nada
	public void cont_dc() {
		if (token.getClasse() == Classe.cId) {
			dvar();
			mais_dc();
		}
	}

    //<variaveis> : <tipo_var>
    public void dvar(){
        variaveis();
        if (token.getClasse() == Classe.cDoisPontos) {
			lerToken();
			tipo_var();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o : no dvar");
		}
    }

    //integer
    public void tipo_var() {
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("integer"))) {
			lerToken();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou a declaração de Integer no tipo_var");
		}
	}

    //<id> {A2} <mais_var>
    public void variaveis() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A2}
			mais_var();			
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o identificador no variáveis");
		}
	}

    //,  <variaveis> | nada
	public void mais_var() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			//{A2}
			variaveis();			
		}
	}

    //<sentencas> ::= <comando> <mais_sentencas> 
	public void sentencas() {
		comando();
		mais_sentencas();
	}

    //<mais_sentencas> ::=  ; <cont_sentencas>
	public void mais_sentencas() {
		if (token.getClasse() == Classe.cPontoVirgula) {
			lerToken();
			cont_sentencas();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o ; no mais_sentencas");
		}
	}

    //<cont_sentencas> ::= <sentencas> | vazio
	public void cont_sentencas() {
		if (((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("read"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("write"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("for"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("repeat"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("while"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("if"))) ||
				((token.getClasse() == Classe.cId))
				) {
			sentencas();
		}
	}


    //<var_read> ::= <id> {A5} <mais_var_read> 
	public void var_read() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A5}
			mais_var_read();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o identificador no var_read");
		}
	}
	
	//<mais_var_read> ::=  ,  <var_read> | vazio
	public void mais_var_read() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			var_read();
		}
	}
	
	
	//<var_write> ::= <id> {A6} <mais_var_write> 
	public void var_write() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A6}
			mais_var_write();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o identificador no var_write");
		}
	}
	
	//<mais_var_write> ::=  ,  <var_write> | vazio
	public void mais_var_write() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			var_write();
		}
	}


    /* 
	read ( <var_read> ) |
    write ( <var_write> ) |
    for <id> {A25} := <expressao> {A26} to {A27} <expressao> {A28}
    do begin <sentencas> end {A29} |
    repeat {A23} <sentencas> until ( <condicao> ) {A24} |
    while {A20} ( <condicao> ) {A21} do begin <sentencas> end {A22} |
    if ( <condicao> ) {A17} then begin <sentencas> end {A18}
    <pfalsa> {A19} |
    <id> {A13} := <expressao> {A14} 

	*/
	public void comando() {
		//read ( <var_read> )
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("read"))){
			lerToken();
			if (token.getClasse() == Classe.cParEsq) {
				lerToken();
				var_read();
				if (token.getClasse() == Classe.cParDir) {
					lerToken();
				}else {
					System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o ( no comando após o var_read do read");
				}
			}else {
				System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o ( no comando após o read");
			}
		}else
		//write ( <var_write> )
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("write"))){
			lerToken();
			if (token.getClasse() == Classe.cParEsq) {
				lerToken();
				var_write();
				if (token.getClasse() == Classe.cParDir) {
					lerToken();
				}else {
					System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o ) no comando após o var_write do write");
				}
			}else {
				System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o ( no comando após o write");
			}
		}else
		//for <id> {A25} := <expressao> {A26} to {A27} <expressao> {A28} 
		//do begin <sentencas> end {A29}
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("for"))){
			lerToken();
			if (token.getClasse() == Classe.cId) {
				lerToken();
				//{A25}
				if (token.getClasse() == Classe.cAtribuicao){
					lerToken();
					expressao();
					//{A26}
					if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("to"))){
						lerToken();
						//{A27}
						expressao();
						//{A28}
						if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("do"))){
							lerToken();
							if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
								lerToken();
								sentencas();
								if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
									lerToken();
									//{A29}									
								}else {
                                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o End no fim do for");
								}								
							}else {
                                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o Begin após o Do do for");
							}							
						}else {
                            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o Do no meio do for");
						}					
					}else {
                        System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o To no meio do for");
					}					
				}else {
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o := no meio do for");
				}
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o identificador no início do for");
			}
		}else
		//repeat {A23} <sentencas> until ( <condicao> ) {A24}
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("repeat"))){
			lerToken();
			//{A23}
			sentencas();
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("until"))){
				lerToken();
				if (token.getClasse() == Classe.cParEsq){
					lerToken();	
					condicao();
					if (token.getClasse() == Classe.cParDir){
						lerToken();
						//{A24}
					}else {
                        System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou fechou o parenteses do repeat após a condição");
					}
				}else {
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou abrir o parentese no repeat após o until");
				}
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o Util após o repeat");
			}				
		}
		//while {A20} ( <condicao> ) {A21} do begin <sentencas> end {A22} |
		else if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("while"))){
			lerToken();	
			//{A20}
			if (token.getClasse() == Classe.cParEsq){
				lerToken();	
				condicao();
				if (token.getClasse() == Classe.cParDir){
					lerToken();
					//{A21}
					if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("do"))){
						lerToken();
						if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
							lerToken();
							sentencas();
							if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
								lerToken();
								//{A22}
							}else {
                                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o end para finalizar o While");
							}
						}else {
                            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o Begin após o Do do while");
						}
					}else {
                        System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o Do dentro do while");
					}
				}else {
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou fechar o parenteses no while");
				}
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou abrir o parenteses no while");
			}
		}
		//if ( <condicao> ) {A17} then begin <sentencas> end {A18} <pfalsa> {A19} |
		else if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("if"))){
			lerToken();
			if (token.getClasse() == Classe.cParEsq){
				lerToken();	
				condicao();
				if (token.getClasse() == Classe.cParDir){
					lerToken();
					//{A17}
					if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("then"))){
						lerToken();
						if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
							lerToken();
							sentencas();
							if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
								lerToken();
								//{A18}
								pfalsa();
								//{A19}
							}else {
                                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o end no fim do if");
							}
						}else {
                            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou begin após o then do if");
						}
					}else {
                        System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o then do if");
					}
				}else {
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou fechar o parenteses no if");
				}
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou abrir o parenteses no if");
			}
		}
		//<id> {A13} := <expressao> {A14} | <chamada_procedimento>
		else if (token.getClasse() == Classe.cId){
			lerToken();
			//{A13}
			if (token.getClasse() == Classe.cAtribuicao){
				lerToken();
				expressao();
				//{A14}
			}else{
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o atribuição do Id do comando");
            }
		}else {
			// chamada_procedimento();
		}
	}


    public void condicao() {
		expressao();
		relacao();
		//{A15}
		expressao();
		//{A16}		
	}
	
	//<pfalsa> ::= else begin <sentencas> end | vazio
	public void pfalsa() {
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("else"))){
			lerToken();
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
				lerToken();
				sentencas();
				if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
					lerToken();
				}else {
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou finalizar o pfalsa com o end");
				}
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o begin após o else do pfalsa");
			}
		}
	}
	
	//<relacao> ::= = | > | < | >= | <= | <>
	public void relacao() {
		if (token.getClasse() == Classe.cIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cMaior) {
			lerToken();
		}else if (token.getClasse() == Classe.cMenor) {
			lerToken();
		}else if (token.getClasse() == Classe.cMaiorIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cMenorIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cDiferente) {
			lerToken();
		}else {
            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o operador matemático de relação");
		}
	}
	
	
	//<expressao> ::= <termo> <outros_termos>
	public void expressao() {
		termo();
		outros_termos();
	}
	
	
	//<outros_termos> ::= <op_ad> {A9} <termo> {A10} <outros_termos>  | vazio
	public void outros_termos() {
		if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
			op_ad();
			//{A9}
			termo();
			//{A10}
			outros_termos();
		}		
	}
	
	
	//<op_ad> ::= + | -
	public void op_ad() {
		if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
			lerToken();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o operador de mais ou de menos no op_ad");
		}
	}
	
	
	//<termo> ::= <fator> <mais_fatores>
	public void termo() {
		fator();
		mais_fatores();
	}
	
	
	//<mais_fatores> ::= <op_mul> {A11} <fator> {A12} <mais_fatores> | vazio
	public void mais_fatores() {
		if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
		op_mul();
		//{A11}
		fator();
		//{A12}
		mais_fatores();
		}
	}
	
	//<op_mul> ::= * | /
	public void op_mul() {
		if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
			lerToken();
		}else {
			System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o operador de multiplicação ou de divisão no op_mul");
		}
	}

    //<fator> ::= <id_var> {A7} | <intnum> {A8} | (<expressao>) | <id_funcao> <argumentos>
	public void fator() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A7}
		}else if (token.getClasse() == Classe.cInt || token.getClasse() == Classe.cReal) {
			lerToken();
			//{A8}
		}else if (token.getClasse() == Classe.cParEsq){
			lerToken();	
			expressao();
			if (token.getClasse() == Classe.cParDir){
				lerToken();
			}else {
                System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou fechar o parenteses da expressão");
			}
		}else {
            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou usar o fator (identificador, numero ou expressão)");
		}
	}

    // verifica se é um identificador
    public void id(){
        if(token.getClasse()==Classe.cId){
            //{A3}
        }else{
            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o id após o program");
        }
    }
}
