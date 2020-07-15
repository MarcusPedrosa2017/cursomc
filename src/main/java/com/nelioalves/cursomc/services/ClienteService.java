package com.nelioalves.cursomc.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.Perfil;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.resources.utils.MultipartImage;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.exception.AuthorizationException;
import com.nelioalves.cursomc.services.exception.DataIntegrityException;
import com.nelioalves.cursomc.services.exception.FileException;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer imageSize;
	
	private static final String JPG_EXTENSION = ".jpeg";
	
	private static final String CONTENT_TYPE = "image";
	
	public ClienteService(ClienteRepository repo) {
		this.repo = repo;
	}
	
	public List<Cliente> findAll() {
		List<Cliente> lista = new ArrayList<>();
		lista = repo.findAll();
		return lista;
	}
	
	public Cliente find(Integer id) {
		
		//pegando o usuario logado
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		
		try{
			
			if(Objects.isNull(obj.get())) {}
			
		}catch(NoSuchElementException e) {
			throw new ObjectsNotFoundException("Objeto não econtrado! ID: " + id + ", Tipo: " + Cliente.class.getName());	
		}
		return obj.get();//DEVIDO AO CONTAINER Optional E NECESSARIO PEGAR O OBJ PELO GET
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEndrecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		//metodo que atualiza dos dados do objeto antes de salvar
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		/*
		 * caso ocorra uma DataIntegrityViolationException do pacote: 
		 * org.springframework.dao.DataIntegrityViolationException;
		 * 
		 * lanço uma do meu pacote de excessao:
		 * com.nelioalves.cursomc.services.exception.DataIntegrityException
		 */
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluír um Cliente que possuí Pedidos Associados!");
		}
		
	}
	
	//lista paginando
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj()
				, TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		
		Optional<Cidade> cid = cidadeRepository.findById(objDto.getCidadeId());
		
		Cidade cidade = new Cidade();
		
		if(Objects.nonNull(cid.get())) {
			if(Objects.nonNull(cid.get().getEstado())) {
				cidade.setEstado(cid.get().getEstado());
			}
			if(Objects.nonNull(cid.get().getId())) {
				cidade.setId(cid.get().getId());
			}
			if(Objects.nonNull(cid.get().getNome())) {
				cidade.setNome(cid.get().getNome());
			}
		}
				
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro()
				, objDto.getCep(), cliente, cidade);
		
		cliente.getEndrecos().add(endereco);
		
		if(Objects.nonNull(objDto.getTelefone1())) {
			cliente.getTelefones().add(objDto.getTelefone1());
		}
		if(Objects.nonNull(objDto.getTelefone2())) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if(Objects.nonNull(objDto.getTelefone3())) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		//recortando imagem
		jpgImage = imageService.cropSquare(jpgImage);
		//redimensionando imagem
		jpgImage = imageService.resize(jpgImage, imageSize);
		
		String fileName = prefix + user.getId() + JPG_EXTENSION;
		Integer size = jpgImage.getRaster().getDataBuffer().getSize();
		
		//utilizando o metodo que criamos no imageService
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, JPG_EXTENSION), fileName, CONTENT_TYPE, Long.valueOf(size.toString()) );		
		
	}
	
	public URI uploadProfilePicture2(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		/*
		//se for imagem recorta e redimensiona o arquivo
		String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		if("jpeg".equals(extName) || "jpg".equals(extName) || "png".equals(extName)) {
			BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
			//recortando imagem
			jpgImage = imageService.cropSquare(jpgImage);
			//redimensionando imagem
			jpgImage = imageService.resize(jpgImage, imageSize);
			//convertendo para MultipartFile
			multipartFile = BufferedImageToMultipartFile(jpgImage, multipartFile.getOriginalFilename(), multipartFile.getName(),
					multipartFile.getContentType(), jpgImage.getRaster().getDataBuffer().getSize());
		}*/
		
		String fileName = prefix + user.getId();
		return s3Service.uploadFilePatternName(multipartFile, fileName);
		
	}
	
	private MultipartFile BufferedImageToMultipartFile(BufferedImage jpgImage, String fileName, String name, String extension, long size) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write( jpgImage, extension, baos );
			baos.flush();
		} catch (IOException e) {
			throw new FileException("Erro ao realizar conversao de BufferedImage para MultipartFile ");
		}		

		MultipartFile multipartFile = new MultipartImage(baos.toByteArray(), fileName, name, extension, size);
		
		return multipartFile;
	}
}
