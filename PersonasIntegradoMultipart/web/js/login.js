var url="http://localhost:8080/PersonasIntegradoMultipart/";

    function login(){
        if (!loginValidar()) return;
        usuario = {
            id: $("#id").val(),
            clave: $("#clave").val()
        };
        
        let request = new Request(url+'api/login', {method: 'POST', headers: { 'Content-Type': 'application/json'},body: JSON.stringify(usuario)});
        (async ()=>{
            const response = await fetch(request);
            if (!response.ok) {errorMessage(response.status,$("#loginDialog #errorDiv"));return;}
            usuario = await response.json();
            sessionStorage.setItem('user', JSON.stringify(usuario));
            $('#loginDialog').modal('hide');            
           switch(usuario.rol){
               case 'ADM': document.location = url+"listado.html"+ "?t="+Math.random(); break;
               case 'CLI': document.location = url+"about.html"; break;
           }                           
        })(); 
    
    }

    function loginValidar(){
        $("#loginForm").addClass("was-validated");
        return $("#loginForm").get(0).checkValidity(); 
    }

    function logout(){
        let request = new Request(url+'api/login', {method: 'DELETE', headers: { }});
        (async ()=>{
            const response = await fetch(request);
            if (!response.ok) {errorMessage(response.status,$("#loginDialog #errorDiv"));return;}
            sessionStorage.removeItem('user');
            document.location = url+"about.html";                         
        })();      
    }

  function errorMessage(status,place){  
        switch(status){
            case 404: error= "Registro no encontrado"; break;
            case 401: case 403: error="Usuario no autorizado"; break;
            case 406: case 405: error="Usuario ya existe"; break;
        };            
        place.html('<div class="alert alert-danger fade show">' +
        '<button type="button" class="close" data-dismiss="alert">' +
        '&times;</button><h4 class="alert-heading">Error!</h4>'+error+'</div>');
        return;        
    }  
  
  function loadLogin(){   
        let request = new Request(url+'login.html', {method: 'GET'});
        (async ()=>{
            const response = await fetch(request);
            if (!response.ok) {errorMessage(response.status,$("#loginDialog #errorDiv"));return;}
            content = await response.text();
            $('body').append(content); 
            $("#login").click(login);
            $("#logout").click(logout);                          
        })();     
  }
  
  $(loadLogin);  