var menu=`
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <a class="navbar-brand" href="#"><img src="images/header.png" style="width: 150px; height: auto;" alt="Registro"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#menu" >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="menu">
        <ul class="navbar-nav ml-auto" id="menuUl">
          <li class="nav-item">
            <a class="nav-link" href="about.html">Nosotros</a>
          </li>`;
          
            let usuarioJson = sessionStorage.getItem('user');
            
            if (usuarioJson!=null){ 
                let usuario= JSON.parse(usuarioJson);
                if (['ADM','CLI'].includes(usuario.rol)){
                    menu+=`<li class='nav-item'> <a class='nav-link' href='listado.html'>Listado</a> </li>`;
                }

                menu+=`
                    <li class='nav-item dropdown'>
                      <a class='nav-link dropdown-toggle' data-toggle='dropdown' href='#'> ${usuario.nombre}</a>
                      <div class='dropdown-menu'>
                        <a class='dropdown-item' id='logout'>Salir</a>
                      </div>
                    </li>`;                
            }
            else{
              menu+=`
                <li class='nav-item'>
                    <a class='nav-link' href='#' data-toggle='modal' data-target='#loginDialog'>Iniciar-Sesión</a>
                </li>`;           
            }
            menu+=`
        </ul>
      </div>
    </div>
  </nav>`;
  
  function loadMenu(){
    $('body').prepend(menu); 
  }
  
  $(loadMenu);  