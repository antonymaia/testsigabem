package br.antony.sigabem.controllers.exception;

import br.antony.sigabem.services.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {


	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<StandardError> missingServletRequestParameter(
			BadRequestException e, HttpServletRequest request){

		StandardError err = new StandardError(
				System.currentTimeMillis(),
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.toString(),
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<StandardError> missingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest request){

		StandardError err = new StandardError(
				System.currentTimeMillis(),
				HttpStatus.BAD_REQUEST.value(),
				"Parâmetro não encontrado na requisição",
				"Parâmetro "+e.getParameterName()+" não encontrado na requisição",
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<StandardError> methodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e, HttpServletRequest request){

		StandardError err = new StandardError(
				System.currentTimeMillis(),
				HttpStatus.BAD_REQUEST.value(),
				"Parâmetro inválido",
				"Parâmetro "+e.getParameter().getParameterName()+" vazio ou inválido",
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}