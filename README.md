# WeekTech - Unicesumar

### 1. Visão Geral

   O sistema Tech Week é uma plataforma mobile para gestão de presença e engajamento em eventos acadêmicos. O projeto utiliza arquitetura nativa Android para garantir performance e precisão na coleta de dados sensíveis de localização e identificação do participante.

### 2. Equipe Técnica e Atribuições

*    Guilherme Tófoli da Silva:
*    Layon:
*    Matheus Coronado:
*    Abner Castanho Cardoso:

### 3. Arquitetura de Software

   O projeto foi estruturado no pacote com.weektech, seguindo padrões de separação de responsabilidades para facilitar a manutenção e escalabilidade:
*    Camada de Visão (XML): Utilização de ConstraintLayout e CardView para interfaces dinâmicas e responsivas.
*    Camada de Adaptação (Adapter): Implementação de RecyclerView com o padrão ViewHolder, otimizando o consumo de memória RAM durante a rolagem de listas.
*    Camada de Navegação: Gerenciamento centralizado de transições via Intents explícitas, devidamente mapeadas no AndroidManifest.xml.

### 4. Validação de Presença via Geofencing (Polígonos)

   O grande diferencial técnico do sistema reside no rigor da confirmação de presença, que abandona o modelo de check-in manual por uma validação geográfica automatizada:
*    Coleta de Coordenadas: O sistema requisita ao hardware do dispositivo as coordenadas de Latitude (LAT) e Longitude (LONG) via API de localização.
*    Mapeamento Prévio: Cada local de palestra (Auditórios, Laboratórios e Salas) possui um polígono geográfico previamente mapeado no sistema, delimitando com precisão o perímetro físico.
*    Algoritmo de Inclusão: No momento do check-in, o sistema executa um cálculo de "Ponto em Polígono". A presença só é autenticada se as coordenadas do aluno estiverem contidas dentro da área geográfica mapeada para aquele evento específico.
*    Segurança: Este método previne fraudes de localização e garante que apenas participantes fisicamente presentes recebam a validação acadêmica.

### 5. Identidade Visual

*    Paleta: Uso predominante de #00467A (Azul Primário), #00A0E8 (Azul Vibrante) e #F0F2F5 (Fundo Neutro) que são as cores da paleta utilizada pela Unicesumar
*    Padrão: Interface baseada em cards flutuantes com sombras suaves e bordas arredondadas, garantindo um aspecto moderno e profissional.

### 6. Considerações de Segurança

*    Roteamento Privado: As telas de Dashboard e Cadastro estão configuradas como não exportadas, impedindo acessos externos não autorizados.
*    Integridade de Dados: O sistema foi projetado para operar com suporte a banco de dados local (Room), garantindo que as validações geográficas possam ser armazenadas e sincronizadas posteriormente.
