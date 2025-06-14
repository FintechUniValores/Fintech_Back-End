// 1. Importar as bibliotecas necessárias
const admin = require("firebase-admin");

// 2. Importar nossa chave de conta de serviço
const serviceAccount = require("./serviceAccountKey.json");

// 3. Inicializar o app Firebase Admin
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

// 4. Obter uma referência ao nosso banco de dados Firestore
const db = admin.firestore();

// =================================================================
// DADOS QUE QUEREMOS INSERIR
// =================================================================

const instructionalCardsData = [
  {
    id: "govbr-level-up",
    data: {
      title: "Como elevar o nível da sua conta",
      steps: [
        "1. Abra o app Gov.br.",
        "2. Valide sua face ou cadastre-se via banco credenciado.",
      ],
      order: 1,
    },
  },
  {
    id: "govbr-2fa-setup",
    data: {
      title: "Habilitar 2 Fatores (2FA)",
      steps: [
        "1. No app Gov.br, vá em 'Segurança'.",
        "2. Ative a 'Verificação em duas etapas’.",
      ],
      order: 2,
    },
  },
];

const guideData = [
  {
    id: "com-pix",
    data: {
      title: "Com Pix Diponível",
      steps: [
        "Selecione uma de suas chaves Pix (campo obrigatório) e informe seus dados pessoais; e",
        "Guarde o número de protocolo, para entrar em contato com a instituição, se necessário.",
        "Observações:",
        "Nesse caso, você receberá o valor em até 12 dias úteis;",
        "Mesmo que você tenha indicado a chave Pix, a instituição pode devolver por TED ou DOC para a conta da chave Pix selecionada; e",
        "A instituição pode entrar em contato pelo telefone ou pelo e-mail indicado por você para confirmar sua identidade ou tirar dúvidas sobre a forma de devolução. Esse é um procedimento para sua segurança e da instituição. Mas não forneça senhas a ninguém.",
      ],
      order: 1,
    },
  },
  {
    id: "sem-pix",
    data: {
      title: "Sem Pix Diponível",
      steps: [
        "Entre em contato diretamente com a instituição financeira pelo telefone ou pelo e-mail informado por ela para combinar a forma de devolução. Nesse caso, a instituição financeira não é obrigada a devolver o valor em até 12 dias úteis; ou",
        "Se preferir, crie uma chave Pix e volte ao sistema para solicitar o valor.",
      ],
      order: 2,
    },
  },
  {
    id: "sem-solicitar-aqui",
    data: {
      title: "Não Ofereceu Solicitar Aqui",
      steps: [
        "Entre em contato diretamente com a instituição financeira pelo telefone ou pelo e-mail informado por ela para combinar a forma de devolução. Nesse caso, a instituição financeira não é obrigada a devolver o valor em até 12 dias úteis.",
      ],
      order: 3,
    },
  },
  {
    id: "observacao-100-2fa",
    data: {
      title: "Observação Sobre Valores Acima De R$ 100 E 2FA",
      steps: [
        "Entre no seu aplicativo gov.br e ative o duplo fator de autenticação, depois acesse novamente o SVR e solicite o resgate do valor normalmente.",
      ],
      order: 4,
    },
  },
];

const faqsData = [
  {
    id: "faq-01",
    data: {
      title: "Não Caia Em Golpes",
      steps: [
        "O único site onde você pode consultar e saber como solicitar a devolução dos seus valores, da sua empresa ou de pessoas falecidas é o https://valoresareceber.bcb.gov.br",
        "Todos os serviços do Valores a Receber são totalmente gratuitos. NÃO faça qualquer tipo de pagamento para ter acesso aos valores.",
        "O Banco Central NÃO envia links NEM entra em contato com você para tratar sobre valores a receber ou para confirmar seus dados pessoais.",
        "Somente a instituição que aparece no Sistema de Valores a Receber é que pode te contatar e ela NUNCA vai pedir sua senha.",
        "NÃO clique em links suspeitos enviados por e-mail, SMS, WhatsApp ou Telegram.",
      ],
      order: 1,
    },
  },
  {
    id: "faq-02",
    data: {
      title: "Sistema de Valores a Receber",
      steps: [
        "O Sistema de Valores a Receber do Banco Central (BC) é uma plataforma que permite aos cidadãos e empresas consultarem se possuem dinheiro esquecido em bancos e outras instituições fiscalizadas pelo BC e, em caso positivo, verifiquem como solicitá-lo.",
        "Não precisa pagar nada para consultar e solicitar os valores. O serviço do BC é 100% gratuito.",
      ],
      order: 2,
    },
  },
  {
    id: "faq-03",
    data: {
      title: "Consultar valores a receber",
      steps: [
        "Você pode saber se tem dinheiro esquecido em banco ou em outra instituição financeira pelo site valoresareceber.bcb.gov.br/publico. Não precisa fazer login. Basta informar seu CPF e sua data de nascimento ou o CNPJ e a data de abertura da empresa, inclusive para empresas encerradas.",
        "Também é possível consultar valores de pessoa falecida, informando o CPF e data de nascimento dela.",
      ],
      order: 3,
    },
  },
  {
    id: "faq-04",
    data: {
      title: "Resgatar valores a receber",
      steps: [
        "Se consultou e viu que tem valores a receber, você pode consultar  a página da internet do Banco  Central (BC) e acessar o Sistema de Valores a Receber (SVR).",
        "Dentro do Sistema de Valores a Receber (SVR), você vai poder verificar quanto de dinheiro há a receber, a origem desse valor, a instituição que deve devolver o valor e seus dados de contato e outras informações adicionais, quando for o caso.",
        "Existem três formas para receber o valor informado no SVR:",
        "1. entre em contato diretamente com a instituição responsável pelo valor e solicite o recebimento;",
        "2. faça a solicitação pelo sistema, desde que a instituição que tem que devolver os valores tenha aderido ao Termo do BC e você possua uma chave Pix (Essa chave não pode ser a aleatória). O BC publica apenas a lista de instituições que aderiram (voluntariamente) ao Termo de adesão para devolução facilitada de valores via Pix. O arquivo é atualizado semanalmente no ícone Termo de Adesão, na linha do Sistema de Informações de Valores a Receber (SVR), do Leiaute de arquivos e base normativa.",
        "para resgatar os valores, há a necessidade da Conta Gov.Br níveis prata ou ouro com verificação em 2 etapas habilitada. Após esses procedimentos, o usuário poderá selecionar sua chave Pix e solicitar o resgate normalmente;",
        "a Conta Gov.Br é de responsabilidade do Ministério da Gestão e da Inovação em Serviços Públicos (MGI). Se precisar de ajuda, acesse o site Dúvidas na Conta Gov.Br.",
        "3. O SVR também possui a funcionalidade de solicitação automática de resgate de valores, para pessoas físicas (PF), com Chave Pix CPF. Após habilitar essa opção na sua página inicial do SVR, siga os próximos passos, como autorizar o uso da Chave Pix CPF e informar seu telefone e e-mail, visualizando ao final a página de confirmação da habilitação. Dessa forma, é possível receber os valores automaticamente na sua conta, das instituições que aderiram a essa funcionalidade, sem precisar fazer os resgates de valores individuais manualmente e ainda receber novos valores que surgirem, enquanto a habilitação estiver ativa.",
      ],
      order: 4,
    },
  },
  {
    id: "faq-05",
    data: {
      title: "Solicitação automática de resgate de valores a receber",
      steps: [
        "Pessoas físicas (PF), com chave Pix CPF, podem acessar no Sistema Valores a Receber (SVR) a funcionalidade de solicitação automática de resgate de valores. Com ela habilitada, você não precisará consultar o sistema periodicamente, nem registrar manualmente a solicitação de cada valor que existe em seu nome.",
        "Para fazer adesão a essa funcionalidade, é necessário seguir os seguintes passos:",
        "Acessar o sistema pelo site https://valoresareceber.bcb.gov.br  e fazer o login com a sua conta gov.br, nível ouro ou prata e com verificação em duas etapas (2FA) habilitada;",
        "Em seguida, basta marcar a opção 'receber valores automaticamente' no campo da Solicitação automática via SVR e seguir os próximos passos, como autorizar o uso da chave Pix CPF e informar o seu telefone e e-mail.",
        "Atenção! As instituições que não aderiram ao Termo de Adesão para devolução via Pix continuarão exigindo solicitação manual. Isso também se aplica a valores oriundos de contas conjuntas.",
      ],
      order: 5,
    },
  },
  {
    id: "faq-06",
    data: {
      title: "Quais as mudanças no Sistema de Valores a Receber (SVR)?",
      steps: [
        "Em 13 de fevereiro de 2025, o acesso ao Sistema de Valores a Receber (SVR) passou a exigir Conta Gov.Br níveis ouro ou prata com a verificação em duas etapas (2FA) habilitada. A exigência do duplo fator de autenticação já existia para consulta de valores acima de R$100.",
        "Com isso, você tem mais segurança e evita o acesso indevido na sua Conta Gov.Br. Saiba que será necessário gerar um código no aplicativo Gov.br para acessar o serviço.",
        "A Conta Gov.Br é de responsabilidade do Ministério da Gestão e da Inovação em Serviços Públicos (MGI). Se precisar de ajuda, acesse o site Dúvidas na Conta Gov.Br.",
        "Em maio de 2025, o SVR passou a ter a funcionalidade de solicitação automática de resgate de valores para pessoas físicas (PF), com chave Pix CPF. Com ela habilitada, é possível que você receba os valores automaticamente na sua conta, sem precisar resgatá-los manualmente.",
      ],
      order: 6,
    },
  },
  {
    id: "faq-07",
    data: {
      title: "Dificuldade para solicitar valores de empresa",
      steps: [
        "Para acessar informações da empresa no Sistema de Valores a Receber (SVR), tais como montante, instituição que deve devolver o valor e origem do valor, a pessoa física (PF) que possua o certificado digital da pessoa jurídica (PJ) deve:",
        "a) realizar o cadastro no Gov.br como pessoa física, caso ainda não tenha conta;",
        "b) acionar o certificado digital de pessoa jurídica ao computador (tipo A1 - máquina - ou tipo A3 - token ou certificado em nuvem);",
        "c) selecionar opção certificado digital ou certificado digital em nuvem na tela inicial do acesso da Conta Gov.Br;",
        "d) seguir os procedimentos específicos do tipo de certificado digital selecionado para digitar senha e continuar o processo;",
        "e) selecionar menu vincular empresas via e-CNPJ, link gerenciar vínculos com empresas;",
        "f) clicar no botão vincular empresa do e-CNPJ; e",
        "g) conferir as informações presentes no certificado digital de pessoa jurídica. Clicar no botão vincular.",
        "Além dos procedimentos acima, há a necessidade de ativar a verificação em duas etapas (2FA).",
        "A Conta Gov.Br é de responsabilidade do Ministério da Gestão e da Inovação em Serviços Públicos (MGI). Se precisar de ajuda, acesse o site Dúvidas na Conta Gov.Br",
        "Atenção! Empresas ativas sem certificado digital não podem acessar o SVR. Os representantes devem contatar diretamente os bancos e instituições com os quais a empresa tem ou teve relacionamento para verificar se há valores a receber.",
      ],
      order: 7,
    },
  },
  {
    id: "faq-08",
    data: {
      title: "Consulta a valores de pessoas falecidas",
      steps: [
        "Para verificar se há dinheiro esquecido em bancos ou outras instituições financeiras de uma pessoa falecida, acesse o Sistema de Valores a Receber (SVR) no site do Banco Central (BC). Não é necessário fazer login. Basta informar o CPF e a data de nascimento da pessoa falecida.",
        "No Sistema de Valores a Receber (SVR), clique no botão 'Valores a receber de pessoa falecida' e aceite o Termo de Responsabilidade de consulta a dados de terceiros.",
        "Atenção! Você precisa ser herdeiro(a), testamentário(a), inventariante ou representante legal, para acessar os dados da pessoa falecida.",
        "Em seguida, informe CPF e data de nascimento da pessoa falecida e você terá acesso a mais informações.",
        "O sistema informa em qual instituição estão os valores dos falecidos, os dados de contato da instituição e a faixa do valor, segundo a tabela abaixo:",
        "a)de R$ 0,01 a R$10,00",
        "b)de R$ 10.01 a R$100,00",
        "c)de R$ 100,01 a R$ 1.000,00",
        "d)acima de R$ 1.000,01",
        "De posse dessas informações, você pode entrar em contato com a instituição e combinar a forma de apresentar a documentação necessária para comprovar que você é herdeiro(a), testamentário(a), inventariante ou representante legal do falecido.",
        "Nesse caso, não é possível solicitar o valor diretamente pelo sistema. Não há prazo definido em norma para devolução de valor de falecido. Após consultar os dados no Sistema de Valores a Receber (SVR), a pessoa deverá entrar em contato com a instituição e questionar a documentação necessária a ser apresentada, bem como combinar a forma de devolução.",
        "O sistema traz uma lista de documentos mínimos, mas as instituições podem solicitar outros que entenderem necessários.",
      ],
      order: 8,
    },
  },
  {
    id: "faq-09",
    data: {
      title: "Valores de empresas encerradas",
      steps: [
        "No Sistema de Valores a Receber (SVR), clique no botão 'Valores para empresas encerradas' e aceite o Termo de Responsabilidade de consulta a dados de terceiros.",
        "Atenção! Você precisa ser representante legal para acessar os dados da empresa encerrada.",
        "Em seguida, informe CNPJ completo e data da abertura da empresa e você terá acesso a mais informações. ",
        "O sistema informa em qual instituição estão os valores da empresa com o CNPJ inativo, os dados de contato da instituição e a faixa do valor, segundo a tabela abaixo: ",
        "1. de R$ 0,01 a R$10,00",
        "2. de R$ 10.01 a R$100,00",
        "3. de R$ 100,01 a R$ 1.000,00",
        "4. acima de R$ 1.000,01",
        "De posse dessas informações, você pode entrar em contato com a instituição e combinar a forma de apresentar a documentação necessária para comprovar que você é representante legal da empresa encerrada.",
        "Nesse caso, não é possível solicitar o valor diretamente pelo sistema.",
      ],
      order: 9,
    },
  },
  {
    id: "faq-10",
    data: {
      title: "Valores a receber de conta conjunta",
      steps: [
        "Os valores de contas conjuntas solidárias (tipo 'ou') estão disponíveis no Sistema de Valores a Receber para solicitação por qualquer titular.",
        "Após efetuada a solicitação por um dos titulares, todos terão acesso ao comprovante da solicitação disponível na seção 'Valores solicitados em conta conjunta' do sistema.",
        "Os valores contas conjuntas não-solidárias (tipo 'e') não são apresentados no Sistema de Valores a Receber nesse momento. Caso você tenha conhecimento de algum valor referente a esse tipo de conta, procure diretamente o banco ou instituição. ",
        "Atenção! O sistema exibe a aba 'Valores de conta conjunta' para todos os usuários, mesmo para aqueles que não tenham conta conjunta. A aba funciona como comprovante, para mostrar todos os valores de conta conjunta que já foram solicitados. Se o usuário não possui conta conjunta, não aparecerá nenhuma informação nessa aba",
      ],
      order: 10,
    },
  },
  {
    id: "faq-11",
    data: {
      title: "Seleção de chave Pix para solicitar o valor",
      steps: [
        "Para solicitar a devolução diretamente pelo Sistema de Valores a Receber (SVR), é necessário que:",
        "a instituição que deve te devolver o valor tenha aderido ao termo de adesão do Banco Central (BC); e",
        "você possua pelo menos uma chave Pix (não pode ser a chave Pix aleatória) e a selecione no sistema.",
        "Se você não tem e prefere não criar nenhuma chave Pix, deve entrar em contato com a instituição para combinar a forma de recebimento. Os canais de atendimento da instituição estão disponíveis na página 'Meus Valores a Receber' no SVR.",
        "O BC publica apenas a lista de instituições que aderiram (voluntariamente) ao Termo de adesão para devolução facilitada de valores via Pix. O arquivo é atualizado semanalmente no ícone Termo de Adesão, na linha do Sistema de Informações de Valores a Receber (SVR), do Leiaute de arquivos e base normativa.",
        "Ainda, foi lançada, em maio de 2025, a funcionalidade de solicitação automática de resgate de valores para pessoas físicas (PF), com chave Pix CPF. Com ela habilitada na sua página do SVR, é possível que você receba os valores automaticamente na conta, sem precisar resgatá-los manualmente.",
      ],
      order: 11,
    },
  },
  {
    id: "faq-12",
    data: {
      title: "Prazo para o banco ou instituição financeira devolver o valor",
      steps: [
        "A devolução é de responsabilidade da instituição e caso você tenha solicitado a devolução via Sistema de Valores a Receber, você receberá o valor em até 12 dias úteis.",
        "Nas solicitações feitas diretamente na instituição, inclusive para valores de pessoas falecidas, não há prazo máximo para a devolução. Dependerá do que for combinado entre você e a instituição e dos prazos de análise dela. ",
      ],
      order: 12,
    },
  },
  {
    id: "faq-13",
    data: {
      title: "Devolução de valor diferente do que aparece no sistema",
      steps: [
        "O valor efetivamente recebido pode ser diferente do informado no Sistema de Valores a Receber (SVR) em razão de alguma atualização monetária ou de descontos previstos em contrato, em lei ou em norma do Sistema Financeiro Nacional (SFN).",
      ],
      order: 13,
    },
  },
  {
    id: "faq-14",
    data: {
      title: "Devolução de valores de cota capital de cooperativa de crédito",
      steps: [
        "Caso um dos valores a receber seja referente a cotas de capital de ex-participantes de cooperativas de crédito, essas instituições podem devolver o valor em uma única vez, pelo valor total ou parcelar a devolução desse valor.",
        "Se a devolução for parcelada, a instituição informará mensalmente ao Sistema de Valores a Receber apenas os valores referentes às parcelas vencidas até aquele mês. Você poderá consultar o sistema todo o mês para solicitar o valor ou pode procurar a cooperativa responsável pelo valor para buscar uma forma de receber os valores automaticamente. ",
      ],
      order: 14,
    },
  },
  {
    id: "faq-15",
    data: {
      title: "Valores a receber por cooperativas de crédito",
      steps: [
        "​A Lei Complementar nº 130/2009 foi alterada para permitir o direcionamento dos valores de cooperados para a reserva patrimonial. Esse ajuste não altera a obrigação das cooperativas de continuarem informando os valores a devolver no Sistema de Valores a Devolver (SVR) para ex-cooperados demitidos, eliminados ou excluídos do quadro social, mesmo após o direcionamento dos recursos para a conta de reserva patrimonial na contabilidade da cooperativa, até o efetivo pagamento ou prescrição do valor conforme o artigo 205 do Código Civil e jurisprudência do Superior Tribunal de Justiça.",
        "O prazo de prescrição do valor é definido em lei como sendo de 10 anos. Nesse período, o pagamento do valor a devolver poderá ser suspenso se a cooperativa de crédito não cumprir com os limites mínimos que deve manter de Patrimônio Líquido. Essa suspensão é temporária, vale enquanto durar o não cumprimento e precisa ser aprovada em Assembleia.",
      ],
      order: 15,
    },
  },
  {
    id: "faq-16",
    data: {
      title:
        "Ligações ou e-mail de bancos ou instituições financeiras após a solicitação de devolução dos valores",
      steps: [
        "A instituição pode entrar em contato pelo telefone ou pelo e-mail informado por você no Sistema de Valores a Receber para confirmar sua identidade ou tirar dúvidas sobre a forma de devolução. Esse é um procedimento para sua segurança e da instituição.",
        "Atenção! Somente essa instituição pode te contatar para tratar apenas do valor informado no sistema e ela NUNCA vai pedir sua senha. Desconfie de outras ligações ou e-mails.",
      ],
      order: 16,
    },
  },
  {
    id: "faq-17",
    data: {
      title:
        "Solicitação do mesmo valor mais de uma vez após a devolução não ter sido concluída",
      steps: [
        "Após a solicitação do valor no Sistema de Valores a Receber (SVR) para pagamento via Pix, o sistema bloqueia novo pedido para evitar que a instituição receba pedidos duplicados.",
        "Mesmo que a instituição tenha que pagar ao credor em até 12 dias úteis, o prazo de bloqueio para solicitação de um valor solicitado que não foi devolvido varia de 30 a 90 dias, a depender do tipo de instituição, devido ao processo de envio de informações de valores a devolver (trimestral, para consórcios, e mensal, para os demais casos).",
        "Certifique-se de ter realizado a solicitação diretamente no SVR ou no banco ou instituição financeira que deve te devolver o valor. Nenhuma pessoa ou empresa está autorizada a atuar como intermediário para a devolução dos valores.",
        "Se você solicitou e não recebeu o valor, recomendamos entrar em contato diretamente com a instituição para verificar o ocorrido. Se necessário, você pode registrar uma reclamação no Banco Central.",
      ],
      order: 17,
    },
  },
  {
    id: "faq-18",
    data: {
      title: "Responsabilidade pelas informações que aparecem no sistema",
      steps: [
        "Os bancos, as administradoras de consórcio e demais instituições autorizadas pelo Banco Central (BC) são responsáveis pelas informações que aparecem no Sistema de Valores a Receber. O BC apenas centraliza as informações para facilitar seu acesso.",
      ],
      order: 18,
    },
  },
  {
    id: "faq-19",
    data: {
      title:
        "Atualização dos valores informados no Sistema de Valores a Receber (SVR)",
      steps: [
        "As informações do SVR estão sempre mudando. As instituições revisam suas bases de dados e enviam periodicamente ao Banco Central valores a devolver de situações antigas que não estavam no sistema anteriormente ou encontram novos valores a devolver decorrentes de situações mais recentes. Assim, recomendamos fazer novas consultas periodicamente para verificar se você tem valores a receber. ",
      ],
      order: 19,
    },
  },
  {
    id: "faq-20",
    data: {
      title:
        "Tipo de valores a receber que aparecem no Sistema de Valores a Receber (SVR)",
      steps: [
        "Os valores que aparecem tanto na consulta pública quanto após o login no SVR são referentes aos valores esquecidos de pessoas físicas e jurídicas referentes a:",

        "1. contas correntes ou poupança encerradas com saldo disponível;",
        "2. cotas de capital e rateio de sobras líquidas de ex-participantes de cooperativas de crédito;",
        "3. recursos não procurados de grupos de consórcio encerrados;",
        "4. tarifas cobradas indevidamente;",
        "5. parcelas ou despesas de operações de crédito cobradas;",
        "6. contas de pagamento pré-paga ou pós-paga (inclusive cartões de crédito) encerradas com saldo disponível;",
        "7. contas de registro mantidas por corretoras e distribuidoras encerradas com saldo disponível; e",
        "8. outros recursos disponíveis nas instituições para devolução.",
        "Para verificar qual a origem do valor esquecido, você deve o login no sistema e clicar no botão 'Meus Valores a Receber', nessa página é apresentada a tabela de informações com a origem do valor. ",
      ],
      order: 20,
    },
  },
  {
    id: "faq-21",
    data: {
      title:
        "Tipo de valores a receber que não estão no Sistema de Valores a Receber (SVR)",
      steps: [
        "O SVR não mostra valores de:",
        "ajustes de planos econômicos;",
        "acordos ou sob disputa judicial;",
        "instituições financeiras ou administradoras de consórcios incorporadas, liquidadas ou encerradas;",
        "valores de abono salarial (PIS ou PASEP);",
        "saldo em conta de FGTS;",
        "contas abertas que estão sem movimentação;",
        "valores de contas inativas não recolhidos ao Tesouro Nacional nos termos da Lei e contas de poupança inativas, independentemente do tempo sem movimentar;",
        "contas sem identificação completa e que não foram recadastradas até dez/1994; e",
        "saldos a devolver em que não é possível associar o CPF ou CNPJ do beneficiário.",
        "O sistema também não informa valores de instituições que entraram em processo de falência ou que não prestam mais serviços financeiros. Nesses casos, busque informações no Poder Judiciário, na Junta Comercial ou na Receita Federal.",
      ],
      order: 21,
    },
  },
];

const productsData = [
  {
    id: "renda-fixa",
    data: {
      title: "Renda Fixa",
      icon: "money-bill-wave",
      features: [
        "Isenção de IR",
        "Liquidez",
        "Previsibilidade",
        "Garantia do FGC",
      ],
      link: "https://www.bancoparceiro.com.br/investimentos/renda-fixa",
    },
  },
  {
    id: "poupanca-facil",
    data: {
      title: "Poupança Fácil",
      icon: "piggy-bank",
      features: [
        "Isenção de IR",
        "Liquidez diária",
        "Garantia do FGC",
        "Cobertura automática",
      ],
      link: "https://www.bancoparceiro.com.br/servicos/poupanca",
    },
  },
];

// =================================================================
// FUNÇÃO QUE EXECUTA O SEED
// =================================================================

async function seedDatabase() {
  console.log("Iniciando o seeding do banco de dados...");

  try {
    // Usamos Promise.all para executar todas as inserções em paralelo
    await Promise.all([
      ...instructionalCardsData.map((item) =>
        db.collection("instructional_cards").doc(item.id).set(item.data)
      ),
      ...guideData.map((item) =>
        db.collection("guides").doc(item.id).set(item.data)
      ),
      ...faqsData.map((item) =>
        db.collection("faqs").doc(item.id).set(item.data)
      ),
      ...productsData.map((item) =>
        db.collection("products").doc(item.id).set(item.data)
      ),
    ]);
    console.log("✅ Banco de dados populado com sucesso!");
  } catch (error) {
    console.error("❌ Erro ao popular o banco de dados:", error);
  }
}

// Executa a função
seedDatabase();
